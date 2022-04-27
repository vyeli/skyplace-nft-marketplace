package ar.edu.itba.paw.service;

import org.springframework.scheduling.annotation.Async;

public interface MailingService {

    @Async
    void sendOfferMail(String buyerEmail, String sellerMail, String nftName, String nftAddress, float nftPrice);

    @Async
    void sendRegisterMail(String userEmail, String username);

}
