package ar.edu.itba.paw.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailingServiceImpl implements MailingService{

    final String username = "keely.blanda37@ethereal.email";
    final String password = "P4cfwdh3Rn43RNr8cn";

    public MailingServiceImpl(){

    }

    public Properties getProperties(){
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.ethereal.email");    // host
        properties.put("mail.smtp.port", "587");    // port
        properties.put("mail.smtp.auth", "true");    // auth
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        return properties;
    }

    @Override
    public void sendMail() {
        // 1. Crear objeto sesion
        Session session = Session.getInstance(getProperties(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // 2. Crear mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(username));
            message.setSubject("Testing mail");
            message.setText("Hello from the other side");

            // 3. Mandar el mensaje
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
