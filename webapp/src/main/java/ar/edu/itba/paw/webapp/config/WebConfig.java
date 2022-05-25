package ar.edu.itba.paw.webapp.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

@ComponentScan({
        "ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.service",
        "ar.edu.itba.paw.persistence"
})
@EnableAsync
@EnableWebMvc
@Configuration
@EnableTransactionManagement
public class WebConfig extends WebMvcConfigurationSupport {

    @Value("classpath:sql/schema.sql")
    private Resource schemaSql;
    public static final String DB_URL_PARAMETER = "DB_URL";
    public static final String DB_USERNAME_PARAMETER = "DB_USERNAME";
    public static final String DB_PASSWORD_PARAMETER = "DB_PASSWORD";

    private final static String MAIL_USERNAME_PARAMETER = "MAIL_USERNAME";
    private final static String MAIL_PASSWORD_PARAMETER = "MAIL_PASSWORD";
    private final static String MAIL_HOST_PARAMETER = "MAIL_HOST";
    private final static String MAIL_PORT_PARAMETER = "MAIL_PORT";
    private final static String MAIL_AUTH_PARAMETER = "MAIL_HAS_AUTH";
    private final static String MAIL_STARTTLS_PARAMETER = "MAIL_STARTTLS_ENABLE";

    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver resolver = new InternalResourceViewResolver();

        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/");
        resolver.setSuffix(".jsp");

        return resolver;
    }

    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        final Dotenv env = Dotenv.load();

        ds.setDriverClass(org.postgresql.Driver.class);

        ds.setUrl(env.get(DB_URL_PARAMETER));
        ds.setUsername(env.get(DB_USERNAME_PARAMETER));
        ds.setPassword(env.get(DB_PASSWORD_PARAMETER));

        return ds;
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        resolver.setDefaultEncoding("utf-8");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/image/")
                .addResourceLocations("/js/");
    }

    @Override
    @Bean
    public HandlerMapping resourceHandlerMapping() {
        AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) super.resourceHandlerMapping();
        handlerMapping.setOrder(-1);
        return handlerMapping;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setCacheSeconds(5);
        return messageSource;
    }

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

    @Bean
    public DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaSql);

        return populator;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        DataSourceInitializer dsi = new DataSourceInitializer();
        dsi.setDataSource(dataSource());
        dsi.setDatabasePopulator(databasePopulator());

        return dsi;
    }

}
