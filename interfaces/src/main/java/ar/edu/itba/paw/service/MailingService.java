package ar.edu.itba.paw.service;

import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;

public interface MailingService {

    @Async
    void sendOfferMail(String buyerEmail, String sellerMail, String nftName, String nftAddress, BigDecimal nftPrice);

    @Async
    void sendRegisterMail(String userEmail, String username);

}
