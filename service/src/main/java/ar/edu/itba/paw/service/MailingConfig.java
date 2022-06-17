package ar.edu.itba.paw.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class MailingConfig {

    private final static String MAIL_USERNAME_PARAMETER = "MAIL_USERNAME";
    private final static String MAIL_PASSWORD_PARAMETER = "MAIL_PASSWORD";
    private final static String MAIL_HOST_PARAMETER = "MAIL_HOST";
    private final static String MAIL_PORT_PARAMETER = "MAIL_PORT";
    private final static String MAIL_AUTH_PARAMETER = "MAIL_HAS_AUTH";
    private final static String MAIL_STARTTLS_PARAMETER = "MAIL_STARTTLS_ENABLE";

    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:mails/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public JavaMailSender emailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        final Dotenv env = Dotenv.load();

        mailSender.setHost(env.get(MAIL_HOST_PARAMETER));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(env.get(MAIL_PORT_PARAMETER))));
        mailSender.setUsername(env.get(MAIL_USERNAME_PARAMETER));
        mailSender.setPassword(env.get(MAIL_PASSWORD_PARAMETER));

        Properties javaMailProps = new Properties();
        javaMailProps.setProperty("mail.smtp.auth", env.get(MAIL_AUTH_PARAMETER));
        javaMailProps.setProperty("mail.smtp.starttls.enable", env.get(MAIL_STARTTLS_PARAMETER));
        mailSender.setJavaMailProperties(javaMailProps);

        return mailSender;
    }

}

