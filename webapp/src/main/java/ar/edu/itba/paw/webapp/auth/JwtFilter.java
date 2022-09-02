package ar.edu.itba.paw.webapp.auth;

import com.sun.net.httpserver.BasicAuthenticator;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // TODO: Move this to JwtConfig POJO Object and secret to somewhere else
    private final static String USERAUTH_PREFIX = "Basic";
    private final static String JWTAUTH_PREFIX = "Bearer";
    private final static Key jwtKey = new SecretKeySpec("mySuperSecretKey".getBytes(), SignatureAlgorithm.HS256.getJcaName());

    @Autowired
    private SkyplaceUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 1. get the authentication header. Tokens are supposed to be passed in the authentication header
        String headerContent = request.getHeader(HttpHeaders.AUTHORIZATION);

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

        switch(prefix){
            case USERAUTH_PREFIX:
                System.out.println("Mande un " + prefix + " con credenciales " + credentials);
                // validate user credentails and, if valid, return new jwt auth and refresh tokens
                String decodedCredentials = new String(Base64.getDecoder().decode(credentials));
                String[] userPass = decodedCredentials.split(":", 2);

                System.out.println("Inicio sesion con usuario " + userPass[0] + " y contra " + userPass[1]);

                userDetailsService.autologin(userPass[0], userPass[1]);

                /*
                UserDetails userDetails = userDetailsService.loadUserByUsername(userPass[0]);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, userPass[1], userDetails.getAuthorities());
                Authentication auth = authenticationManager.authenticate(authToken);

                if(auth.isAuthenticated()) {
                    System.out.println("AUTENTIQUE ESAAAAAAAA");
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

                 */

                // add jwt body to response


                /*
                String refreshToken = Jwts.builder()
                        .claim("username", userPass[0])
                        .claim("password", userPass[1])
                        .setIssuer("skyplace")
                        .setSubject(userPass[0])
                        .setExpiration(Date.from(Instant.now().plus(3, ChronoUnit.MINUTES)))
                        .setNotBefore(Date.from(Instant.now()))
                        .setIssuedAt(Date.from(Instant.now()))
                        .setId(UUID.randomUUID().toString())
                        .signWith(jwtKey)
                        .compact();

                 */

                // response.addHeader("Access Token", JwtUtil.generateToken("somebody"));

                /*
                String refreshToken = Jwts.builder().setIssuer("Skyplace").compact();

                response.addHeader("Access Token", "TU VIEJA");
                response.addHeader("Renewal Token", refreshToken);

                 */
                response.addHeader("Access Token", "Some access token");
                response.addHeader("Renewal Token","Some refresh token");

                break;
            case JWTAUTH_PREFIX:
                // validate jwt info sent and, if valid:
                // - if sent jwt auth token     -> proceed with operation
                // - if sent jwt refresh token  -> regenerate jwt auth token and return it to user
                break;
            default:
                break;
        }


        // go to the next filter in the filter chain
        chain.doFilter(request, response);
        /*
        // If there is no token provided and hence the user won't be authenticated.
        // It's Ok. Maybe the user accessing a public path or asking for a token.

        // All secured paths that needs a token are already defined and secured in config class.
        // And If user tried to access without access token, then he won't be authenticated and an exception will be thrown.

        // 3. Get the token
        String token = header.replace(JWTAUTH_PREFIX, "");

        try {	// exceptions might be thrown in creating the claims if for example the token is expired

            // 4. Validate the token
            // Deprecated version
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET.getBytes())
//                    .parseClaimsJws(token)
//                    .getBody();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            if(username != null) {
                @SuppressWarnings("unchecked")
                List<String> authorities = (List<String>) claims.get("authorities");

                // 5. Create auth object
                // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
                // It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

                // 6. Authenticate the user
                // Now, user is authenticated
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
            SecurityContextHolder.clearContext();
        }

        // go to the next filter in the filter chain
        chain.doFilter(request, response);
        */
    }

}
