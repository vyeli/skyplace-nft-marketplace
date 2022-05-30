package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Review;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    // FIXME: no deberia esto ser estatico hardcodeado
    private static final String EXPLORE_URL = "http://pawserver.it.itba.edu.ar/paw-2022a-09/explore";
    private static final String PRODUCT_BASE_URL = "http://pawserver.it.itba.edu.ar/paw-2022a-09/product/";

    private static final String PROFILE_BASE_URL = "http://pawserver.it.itba.edu.ar/paw-2022a-09/profile/";

    private static final String REVIEW_BASE_URL = "http://pawserver.it.itba.edu.ar/paw-2022a-09/review/";

    private final static String CREATE_REVIEW_SUFFIX = "/create";


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

    @Override
    public void sendNftCreatedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, byte[] imageBytes, Locale locale) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(userEmail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("\uD83C\uDF8A Your nft has been created in our page!");
            }
            else {
                helper.setSubject("\uD83C\uDF8A ¡Su nft ha sido creado en nuestra página!");
            }

            Context context = new Context(locale);
            context.setVariable("userEmail", userEmail);
            context.setVariable("username", username);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("productUrl", PRODUCT_BASE_URL + nftId);
            context.setVariable("logoResourceName", "logo");
            context.setVariable("nftImageResourceName", "nftImage");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("nftNameImageResourceName", "nftNameImage");
            context.setVariable("nftAddressImageResourceName", "nftAddressImage");

            String html = templateEngine.process("nftCreated", context);
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

    @Override
    public void sendNftSellOrderCreatedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, BigDecimal nftPrice, byte[] imageBytes, Locale locale) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(userEmail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("\uD83C\uDF8A Your sell order has been created in our page!");
            }
            else {
                helper.setSubject("\uD83C\uDF8A ¡Su orden de venta ha sido creada en nuestra página!");
            }

            Context context = new Context(locale);
            context.setVariable("userEmail", userEmail);
            context.setVariable("username", username);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("nftPrice", nftPrice);
            context.setVariable("productUrl", PRODUCT_BASE_URL + nftId);
            context.setVariable("logoResourceName", "logo");
            context.setVariable("nftImageResourceName", "nftImage");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("nftNameImageResourceName", "nftNameImage");
            context.setVariable("nftAddressImageResourceName", "nftAddressImage");
            context.setVariable("priceImageResourceName", "priceImage");

            String html = templateEngine.process("sellOrderCreated", context);
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

    @Override
    public void sendOfferAcceptedMail(String bidderMail, String sellerMail, int sellerId, String username, String nftName, int nftId, String nftAddress, BigDecimal offerPrice, byte[] imageBytes, Locale locale) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(bidderMail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("Your offer to an NFT has been accepted!");
            }
            else {
                helper.setSubject("¡Tu oferta a un NFT ha sido aceptada!");
            }

            Context context = new Context(locale);
            context.setVariable("sellerMail", sellerMail);
            context.setVariable("bidderMail", bidderMail);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("offerPrice", offerPrice);
            context.setVariable("productUrl", PRODUCT_BASE_URL + nftId);
            context.setVariable("reviewUrl", REVIEW_BASE_URL + sellerId + CREATE_REVIEW_SUFFIX);
            context.setVariable("logoResourceName", "logo");
            context.setVariable("nftImageResourceName", "nftImage");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("nftNameImageResourceName", "nftNameImage");
            context.setVariable("nftAddressImageResourceName", "nftAddressImage");
            context.setVariable("priceImageResourceName", "priceImage");

            String html = templateEngine.process("offerAccepted", context);
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

    @Override
    public void sendOfferRejectedMail(String bidderMail, String sellerMail, String bidderUsername, String nftName, int nftId, String nftAddress, BigDecimal offerPrice, byte[] imageBytes, Locale locale) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(bidderMail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("Your offer to an NFT has been rejected");
            }
            else {
                helper.setSubject("Tu oferta a un NFT ha sido rechazada");
            }

            Context context = new Context(locale);
            context.setVariable("sellerMail", sellerMail);
            context.setVariable("bidderMail", bidderMail);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("offerPrice", offerPrice);
            context.setVariable("productUrl", PRODUCT_BASE_URL + nftId);
            context.setVariable("logoResourceName", "logo");
            context.setVariable("nftImageResourceName", "nftImage");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("nftNameImageResourceName", "nftNameImage");
            context.setVariable("nftAddressImageResourceName", "nftAddressImage");
            context.setVariable("priceImageResourceName", "priceImage");

            String html = templateEngine.process("offerRejected", context);
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



    @Override
    public void sendNftDeletedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, byte[] imageBytes, Locale locale) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(userEmail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("Your nft has been deleted in our page");
            }
            else {
                helper.setSubject("Su nft ha sido eliminado en nuestra página");
            }

            Context context = new Context(locale);
            context.setVariable("userEmail", userEmail);
            context.setVariable("username", username);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("productUrl", PRODUCT_BASE_URL + nftId);
            context.setVariable("logoResourceName", "logo");
            context.setVariable("nftImageResourceName", "nftImage");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("nftNameImageResourceName", "nftNameImage");
            context.setVariable("nftAddressImageResourceName", "nftAddressImage");

            String html = templateEngine.process("nftDeleted", context);
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

    @Override
    public void sendSellOrderDeletedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, byte[] imageBytes, BigDecimal nftPrice, Locale locale) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);
            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(userEmail);

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("Your sell order has been deleted in our page");
            }
            else {
                helper.setSubject("Su orden de venta ha sido eliminada en nuestra página");
            }

            Context context = new Context(locale);
            context.setVariable("userEmail", userEmail);
            context.setVariable("username", username);
            context.setVariable("nftName", nftName);
            context.setVariable("nftId", nftId);
            context.setVariable("nftAddress", nftAddress);
            context.setVariable("nftPrice", nftPrice);
            context.setVariable("productUrl", PRODUCT_BASE_URL + nftId);
            context.setVariable("logoResourceName", "logo");
            context.setVariable("nftImageResourceName", "nftImage");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("nftNameImageResourceName", "nftNameImage");
            context.setVariable("nftAddressImageResourceName", "nftAddressImage");
            context.setVariable("priceImageResourceName", "priceImage");

            String html = templateEngine.process("sellOrderDeleted", context);
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


    @Override
    public void sendNewReviewMail(Review review, Locale locale) {
        final MimeMessage message = emailSender.createMimeMessage();
        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, EMAIL_ENCODING);

            helper.setFrom(env.get(MAIL_USERNAME_PARAMETER));
            helper.setTo(review.getUsersByIdReviewee().getEmail());

            if(!Objects.equals(locale.getLanguage(), "es")) {
                helper.setSubject("New review in your profile!");
            }
            else {
                helper.setSubject("¡Nueva reseña en tu perfil!");
            }

            Context context = new Context(locale);

            context.setVariable("revieweeName", review.getUsersByIdReviewee().getUsername());
            context.setVariable("reviewerName", review.getUsersByIdReviewer().getUsername());
            context.setVariable("reviewerEmail", review.getUsersByIdReviewer().getEmail());
            context.setVariable("profileUrl", PROFILE_BASE_URL + review.getUsersByIdReviewee().getId());
            context.setVariable("score", review.getScore());
            context.setVariable("reviewTitle", review.getTitle());
            context.setVariable("reviewComment", review.getComments());
            context.setVariable("logoResourceName", "logo");
            context.setVariable("userImageResourceName", "userImage");
            context.setVariable("reviewImageResourceName", "reviewImage");
            context.setVariable("reviewScoreImageResourceName", "scoreImage");

            String html = templateEngine.process("newReview", context);
            helper.setText(html, true);

            helper.addInline("logo", new ClassPathResource("/mails/images/logo.png"), IMG_PNG_FORMAT);
            helper.addInline("userImage", new ClassPathResource("/mails/images/user.png"), IMG_PNG_FORMAT);
            helper.addInline("reviewImage", new ClassPathResource("/mails/images/review.png"), IMG_PNG_FORMAT);
            helper.addInline("scoreImage", new ClassPathResource("/mails/images/score.png"), IMG_PNG_FORMAT);

            emailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("Unexpected error sending email", e);
        }
    }
}
