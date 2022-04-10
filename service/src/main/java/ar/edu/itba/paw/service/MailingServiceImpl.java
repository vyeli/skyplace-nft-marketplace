package ar.edu.itba.paw.service;


import io.github.cdimascio.dotenv.Dotenv;
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

    public MailingServiceImpl(){

    }

    public Properties getProperties(){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", env.get(MAIL_HOST_PARAMETER));    // host
        properties.put("mail.smtp.port", env.get(MAIL_PORT_PARAMETER));    // port
        properties.put("mail.smtp.auth", env.get(MAIL_AUTH_PARAMETER));    // auth
        properties.put("mail.smtp.starttls.enable", env.get(MAIL_STARTTLS_PARAMETER)); //TLS
        return properties;
    }

    @Override
    public void sendMail() {
        // 1. Crear objeto sesion
        Session session = Session.getInstance(getProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(env.get(MAIL_USERNAME_PARAMETER), env.get(MAIL_PASSWORD_PARAMETER));
                    }
                });

        try {
            // 2. Crear mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Objects.requireNonNull(env.get(MAIL_USERNAME_PARAMETER))));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(Objects.requireNonNull(env.get(MAIL_USERNAME_PARAMETER))));
            message.setSubject("Testing mail");
            message.setText("Hello from the other side");

            // 3. Mandar el mensaje
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
