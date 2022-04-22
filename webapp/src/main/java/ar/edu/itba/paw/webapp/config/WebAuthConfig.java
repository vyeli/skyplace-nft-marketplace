package ar.edu.itba.paw.webapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import ar.edu.itba.paw.webapp.auth.skyplaceUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private skyplaceUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                    .invalidSessionUrl("/login")
                .and().authorizeRequests()
                    .antMatchers("/login", "/create").anonymous()
                    .antMatchers("/sell").hasRole("USER")
                    .antMatchers("/**").authenticated()
                .and().formLogin()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", false)
                    .loginPage("/login")
                .and().rememberMe()
                    .rememberMeParameter("rememberme")
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                //FIXME ADD OPENSSL 4K KEY FILE AS KEY
//                .key("mysupersecretketthatnobodyknowsabout ") // no hacer esto, crear una aleatoria segura suficiente mente grande y colocarla bajo src/main/resources
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                .and().exceptionHandling()
                    .accessDeniedPage("/403")
                .and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/resources/**", "/403");
    }

}
