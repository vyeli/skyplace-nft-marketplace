package ar.edu.itba.paw.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Objects;


@Service
public class MailingServiceImpl implements MailingService{

    private final Dotenv env = Dotenv.load();

    private final static String MAIL_USERNAME_PARAMETER = "MAIL_USERNAME";

    private static final String EMAIL_ENCODING = "UTF-8";
    private static final String IMG_PNG_FORMAT = "image/png";

    private static final String EXPLORE_URL = "http://pawserver.it.itba.edu.ar/paw-2022a-09/explore";
    private static final String PRODUCT_BASE_URL = "http://pawserver.it.itba.edu.ar/paw-2022a-09/product/";

    private static final Logger LOGGER = LoggerFactory.getLogger(MailingServiceImpl.class);

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public MailingServiceImpl(SpringTemplateEngine templateEngine, JavaMailSender emailSender) {
        this.templateEngine = templateEngine;
        this.emailSender = emailSender;
    }

    @Override
    public void sendRegisterMail(String userEmail, String username, Locale locale){
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);

            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(userEmail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("Welcome to Skyplace");
            }
            else {
                helper.setSubject("Bienvenido a Skyplace");
            }

            Context context = new Context(locale);

            context.setVariable("logoResourceName", "logo");
            context.setVariable("exploreResourceName", "explore");
            context.setVariable("buyResourceName", "buy");
            context.setVariable("sellResourceName", "sell");

            context.setVariable("username", username);
            context.setVariable("exploreUrl", EXPLORE_URL);
            String html = templateEngine.process("register", context);
            helper.setText(html, true);

            helper.addInline("logo", new ClassPathResource("/mails/images/logo.png"), IMG_PNG_FORMAT);
            helper.addInline("explore", new ClassPathResource("/mails/images/explore.png"), IMG_PNG_FORMAT);
            helper.addInline("buy", new ClassPathResource("/mails/images/buy.png"), IMG_PNG_FORMAT);
            helper.addInline("sell", new ClassPathResource("/mails/images/sell.png"), IMG_PNG_FORMAT);

            emailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("Unexpected error sending email", e);
        }
    }

    @Override
    public void sendOfferMail(String bidderMail, String sellerMail, String nftName, int nftId, String nftAddress, BigDecimal nftPrice, byte[] imageBytes, Locale locale){
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(sellerMail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("Bid placed for your NFT");
            }
            else {
                helper.setSubject("Oferta realizada a su NFT");
            }

            Context context = new Context(locale);
            context.setVariable("sellerMail", sellerMail);
            context.setVariable("bidderMail", bidderMail);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("nftBidEthPrice", nftPrice);
            context.setVariable("productUrl", PRODUCT_BASE_URL + nftId);
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

            emailSender.send(message);
        } catch (MessagingException | IOException e) {
            LOGGER.error("Unexpected error sending email", e);
        }
    }

}
