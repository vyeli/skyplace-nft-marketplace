package ar.edu.itba.paw.webapp.auth;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final SkyplaceUserDetailsService userDetailsService;
    private final static Dotenv env = Dotenv.load();

    // TODO: Move this to JwtConfig POJO Object and secret to somewhere else
    private final static String USERAUTH_PREFIX = "Basic";
    private final static String JWTAUTH_PREFIX = "Bearer";
    private final static String JWT_KEY_PARAMETER = "JWT_KEY";

    private final static int accessTokenValidMinutes = 10;
    private final static int refreshTokenValidMinutes = 15;

    private final Key jwtKey; // = new SecretKeySpec("mySuperSecretKeymySuperSecretKey".getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public JwtAuthorizationFilter(SkyplaceUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtKey = new SecretKeySpec(Objects.requireNonNull(env.get("JWT_KEY")).getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        UsernamePasswordAuthenticationToken token;
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String headerContent = request.getHeader(HttpHeaders.AUTHORIZATION);
        Instant now = Instant.now();

        // 2. validate the header and check the prefix
        if(headerContent == null){
            System.out.println("ERROR: No recibi el header de auth");
            // go to the next filter in the filter chain
            chain.doFilter(request, response);
            return;
        }

        String[] contentInfo = headerContent.split(" ", 2);
        if(contentInfo.length < 2){
            System.out.println("ERROR: No recibi las credenciales en el header de auth");
            chain.doFilter(request, response);
            return;
        }

        String prefix = contentInfo[0];
        String credentials = contentInfo[1];

        String accessToken;
        String refreshToken;

        switch(prefix){
            case USERAUTH_PREFIX:
                System.out.println("Mande un " + prefix + " con credenciales " + credentials);
                // validate user credentails and, if valid, return new jwt auth and refresh tokens
                String decodedCredentials = new String(Base64.getDecoder().decode(credentials));
                String[] userPass = decodedCredentials.split(":", 2);

                System.out.println("Inicio sesion con usuario " + userPass[0] + " y contra " + userPass[1]);

                token = userDetailsService.restLogin(userPass[0], userPass[1]);

                if(token.isAuthenticated())
                    SecurityContextHolder.getContext().setAuthentication(token);

                accessToken = Jwts.builder()
                        .claim("user", userPass[0])
                        .claim("token", "access")
                        .setIssuer("skyplace")
                        .setSubject(userPass[0])
                        .setExpiration(Date.from(now.plus(accessTokenValidMinutes, ChronoUnit.MINUTES)))
                        .setNotBefore(Date.from(now))
                        .setIssuedAt(Date.from(now))
                        .setId(UUID.randomUUID().toString())
                        .signWith(jwtKey)
                        .setHeaderParam("typ", Header.JWT_TYPE)
                        .compact();
                refreshToken = Jwts.builder()
                        .claim("user", userPass[0])
                        .claim("token", "refresh")
                        .setIssuer("skyplace")
                        .setSubject(userPass[0])
                        .setExpiration(Date.from(now.plus(refreshTokenValidMinutes, ChronoUnit.MINUTES)))
                        .setIssuedAt(Date.from(now))
                        .setId(UUID.randomUUID().toString())
                        .signWith(jwtKey)
                        .setHeaderParam("typ", Header.JWT_TYPE)
                        .compact();

                response.addHeader("Access Token", accessToken);
                response.addHeader("Renewal Token", refreshToken);
                break;
            case JWTAUTH_PREFIX:
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtKey)
                        .build()
                        .parseClaimsJws(credentials)
                        .getBody();
                if(claims.getExpiration().before(Date.from(now))) {
                    // reject token
                    break;
                }
                switch((String)claims.get("token")) {
                    case "access":
                        String email = claims.getSubject();
                        System.out.println(email);
                        token = userDetailsService.jwtLogin(email);
                        if(token.isAuthenticated())
                            SecurityContextHolder.getContext().setAuthentication(token);
                        break;
                    case "refresh":
                        accessToken = Jwts.builder()
                                .claim("user", claims.getSubject())
                                .claim("token", "access")
                                .setIssuer("skyplace")
                                .setSubject(claims.getSubject())
                                .setExpiration(Date.from(now.plus(accessTokenValidMinutes, ChronoUnit.MINUTES)))
                                .setNotBefore(Date.from(now))
                                .setIssuedAt(Date.from(now))
                                .setId(UUID.randomUUID().toString())
                                .signWith(jwtKey)
                                .setHeaderParam("typ", Header.JWT_TYPE)
                                .compact();
                        refreshToken = Jwts.builder()
                                .claim("user", claims.getSubject())
                                .claim("token", "refresh")
                                .setIssuer("skyplace")
                                .setSubject(claims.getSubject())
                                .setExpiration(Date.from(now.plus(refreshTokenValidMinutes, ChronoUnit.MINUTES)))
                                .setIssuedAt(Date.from(now))
                                .setId(UUID.randomUUID().toString())
                                .signWith(jwtKey)
                                .setHeaderParam("typ", Header.JWT_TYPE)
                                .compact();

                        response.addHeader("Access Token", accessToken);
                        response.addHeader("Renewal Token", refreshToken);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
    }

}
