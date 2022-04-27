package ar.edu.itba.paw.service;


import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;

@Service
public class MailingServiceImpl implements MailingService{

    private final Dotenv env = Dotenv.load();

    private final static String MAIL_USERNAME_PARAMETER = "MAIL_USERNAME";
    private final static String MAIL_PASSWORD_PARAMETER = "MAIL_PASSWORD";
    public static final String MAIL_HOST_PARAMETER = "MAIL_HOST";
    public static final String MAIL_PORT_PARAMETER = "MAIL_PORT";
    public static final String MAIL_AUTH_PARAMETER = "MAIL_HAS_AUTH";
    public static final String MAIL_STARTTLS_PARAMETER = "MAIL_STARTTLS_ENABLE";

    private static final Logger LOGGER = LoggerFactory.getLogger(MailingServiceImpl.class);

    public Properties getProperties(){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", env.get(MAIL_HOST_PARAMETER));    // host
        properties.put("mail.smtp.port", env.get(MAIL_PORT_PARAMETER));    // port
        properties.put("mail.smtp.auth", env.get(MAIL_AUTH_PARAMETER));    // auth
        properties.put("mail.smtp.starttls.enable", env.get(MAIL_STARTTLS_PARAMETER)); //TLS
        return properties;
    }

    @Override
    public void sendOfferMail(String buyerMail, String sellerMail, String nftName, String nftAddress, float nftPrice) {
        Session session = Session.getInstance(getProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.get(MAIL_USERNAME_PARAMETER), env.get(MAIL_PASSWORD_PARAMETER));
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Objects.requireNonNull(env.get(MAIL_USERNAME_PARAMETER))));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sellerMail));
            message.setSubject("Bid placed for your nft");
            message.setText("Hello, " + buyerMail + " has just placed a bid for your nft " + nftName + ". Full details are:" +
                    "\n- Buyer: " + buyerMail +
                    "\n- Nft name: " + nftName +
                    "\n- Nft address: " + nftAddress +
                    "\n- Price: " + nftPrice);

            Transport.send(message);

        } catch (MessagingException e) {
            LOGGER.error("Unexpected error sending email", e);
        }

    }

    @Override
    public void sendRegisterMail(String userEmail, String username) {
        Session session = Session.getInstance(getProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.get(MAIL_USERNAME_PARAMETER), env.get(MAIL_PASSWORD_PARAMETER));
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Objects.requireNonNull(env.get(MAIL_USERNAME_PARAMETER))));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Welcome to Skyplace");
            message.setText("Hello, " + username + " welcome to Skyplace the best nft marketplace." +
                    "\nThank you for registering in our website! ");

            Transport.send(message);

        } catch (MessagingException e) {
            LOGGER.error("Unexpected error sending email", e);
        }

    }

}
