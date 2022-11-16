package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class SkyplaceUserDetailsService implements UserDetailsService {

    private final UserService us;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public SkyplaceUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = us.getUserByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("No such user with email: " + email));
        final List<GrantedAuthority> roles = new ArrayList<>();
        for(String rol:Role.getRoles()) {
            if (user.getRole().name().equals(rol)) {
                System.out.println("User " + email + " has role of " + rol);
                roles.add(new SimpleGrantedAuthority(String.format("ROLE_%s", rol.toUpperCase())));
            }
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
    }

    public UsernamePasswordAuthenticationToken restLogin(String email, String password) throws UsernameNotFoundException {
        final User user = us.getUserByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("No such user with email: " + email));
        final List<GrantedAuthority> roles = new ArrayList<>();
        for(String rol:Role.getRoles()) {
            if (user.getRole().name().equals(rol)) {
                System.out.println("User " + email + " has role of " + rol);
                roles.add(new SimpleGrantedAuthority(String.format("ROLE_%s", rol.toUpperCase())));
            }
        }
        return new UsernamePasswordAuthenticationToken(email, password, roles);
        //return new org.springframework.security.core.userdetails.User(user.getEmail(), password, roles);
    }

    public UsernamePasswordAuthenticationToken jwtLogin(String email) throws UsernameNotFoundException {
        final User user = us.getUserByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("No such user with email: " + email));
        final List<GrantedAuthority> roles = new ArrayList<>();
        for(String rol:Role.getRoles()) {
            if (user.getRole().name().equals(rol)) {
                System.out.println("User " + email + " has role of " + rol);
                roles.add(new SimpleGrantedAuthority(String.format("ROLE_%s", rol.toUpperCase())));
            }
        }
        return new UsernamePasswordAuthenticationToken(email, user.getPassword(), roles);
        //return new org.springframework.security.core.userdetails.User(user.getEmail(), password, roles);
    }

    public void autologin(String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        for(GrantedAuthority authority : userDetails.getAuthorities())
            System.out.println("authority: " + authority);

        Authentication auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        System.out.println("PASO POR ACA");

        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("ENTRO ACA");
        }

    }

}
