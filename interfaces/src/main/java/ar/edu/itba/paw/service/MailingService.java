package ar.edu.itba.paw.service;

import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.Locale;

public interface MailingService {

    @Async
    void sendOfferMail(String bidderEmail, String sellerMail, String nftName, int nftId, String nftAddress, BigDecimal nftPrice, byte[] image, Locale locale);

    @Async
    void sendRegisterMail(String userEmail, String username, Locale locale);

}
