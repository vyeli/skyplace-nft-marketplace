package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SkyplaceLogoutHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserService us;

    @Autowired
    public SkyplaceLogoutHandler(final UserService us) {
        this.us = us;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        us.setCurrentUser(null);
        super.onLogoutSuccess(request, response, authentication);
    }
}
