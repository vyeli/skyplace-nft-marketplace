package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Image;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;

public interface MailingService {

    @Async
    void sendOfferMail(String bidderEmail, String sellerMail, String nftName, int nftId, String nftAddress, BigDecimal nftPrice, byte[] image);

    @Async
    void sendRegisterMail(String userEmail, String username);

}
