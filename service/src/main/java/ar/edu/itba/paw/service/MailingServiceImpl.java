package ar.edu.itba.paw.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Properties;

@Service
public class MailingServiceImpl implements MailingService{

    private final Dotenv env = Dotenv.load();

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    private final static String MAIL_USERNAME_PARAMETER = "MAIL_USERNAME";
    private final static String MAIL_PASSWORD_PARAMETER = "MAIL_PASSWORD";
    private final static String MAIL_HOST_PARAMETER = "MAIL_HOST";
    private final static String MAIL_PORT_PARAMETER = "MAIL_PORT";
    private final static String MAIL_AUTH_PARAMETER = "MAIL_HAS_AUTH";
    private final static String MAIL_STARTTLS_PARAMETER = "MAIL_STARTTLS_ENABLE";

    private static final String EMAIL_ENCODING = "UTF-8";
    private static final String IMG_PNG_FORMAT = "image/png";

    private static final Logger LOGGER = LoggerFactory.getLogger(MailingServiceImpl.class);

    public MailingServiceImpl() {
        this.templateEngine = getMailTemplateEngine();
        this.mailSender = getMailSender();
    }

    private TemplateEngine getMailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(getHtmlTemplateResolver());

        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("mail/MailMessages");

        templateEngine.setTemplateEngineMessageSource(messageSource);
        return templateEngine;
    }

    private ITemplateResolver getHtmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setPrefix("/mails/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Basic mail sender configuration
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

    @Override
    public void sendRegisterMail(String userEmail, String username){
        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(userEmail);
            helper.setSubject("Welcome to Skyplace");

            Context context = new Context();

            context.setVariable("logoResourceName", "logo");
            context.setVariable("exploreResourceName", "explore");
            context.setVariable("buyResourceName", "buy");
            context.setVariable("sellResourceName", "sell");

            context.setVariable("username", username);
            String html = templateEngine.process("register", context);
            helper.setText(html, true);

            helper.addInline("logo", new ClassPathResource("/mails/images/logo.png"), IMG_PNG_FORMAT);
            helper.addInline("explore", new ClassPathResource("/mails/images/explore.png"), IMG_PNG_FORMAT);
            helper.addInline("buy", new ClassPathResource("/mails/images/buy.png"), IMG_PNG_FORMAT);
            helper.addInline("sell", new ClassPathResource("/mails/images/sell.png"), IMG_PNG_FORMAT);

            mailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("Unexpected error sending email", e);
        }
    }

    @Override
    public void sendOfferMail(String bidderMail, String sellerMail, String nftName, long nftId, String nftAddress, BigDecimal nftPrice, byte[] imageBytes){
        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(sellerMail);
            helper.setSubject("Bid placed for your NFT");

            Context context = new Context();
            context.setVariable("sellerMail", sellerMail);
            context.setVariable("bidderMail", bidderMail);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("nftBidEthPrice", nftPrice);
            // TODO: Put here real USD price
            context.setVariable("nftBidUsdPrice", 42.374305);
            context.setVariable("logoResourceName", "logo");
            context.setVariable("nftImageResourceName", "nftImage");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("nftNameImageResourceName", "nftNameImage");
            context.setVariable("nftAddressImageResourceName", "nftAddressImage");
            context.setVariable("priceImageResourceName", "priceImage");

            String html = templateEngine.process("bid", context);
            helper.setText(html, true);

            InputStream is = new BufferedInputStream(new ByteArrayInputStream(imageBytes));
            InputStreamSource imageSource = new ByteArrayResource(imageBytes);

            helper.addInline("logo", new ClassPathResource("/mails/images/logo.png"), IMG_PNG_FORMAT);
            helper.addInline("userImage", new ClassPathResource("/mails/images/user.png"), IMG_PNG_FORMAT);
            helper.addInline("priceImage", new ClassPathResource("/mails/images/sell.png"), IMG_PNG_FORMAT);
            helper.addInline("nftNameImage", new ClassPathResource("/mails/images/nft.png"), IMG_PNG_FORMAT);
            helper.addInline("nftAddressImage", new ClassPathResource("/mails/images/chain.png"), IMG_PNG_FORMAT);
            helper.addInline("nftImage", imageSource, URLConnection.guessContentTypeFromStream(is));

            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            LOGGER.error("Unexpected error sending email", e);
        }
    }

}
