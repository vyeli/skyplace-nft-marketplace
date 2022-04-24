package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class SkyplaceUserDetailsService implements UserDetailsService {

    private final UserService us;

    @Autowired
    public SkyplaceUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = us.getUserByEmail(email).
                orElseThrow(()->new UsernameNotFoundException("No such user with email: " + email));

        us.setCurrentUser(user);
        final Collection<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        // if (user. ...) roles.add(new SimpleGrantedAuthority("ROLE_EDITOR"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), roles);
    }
}
