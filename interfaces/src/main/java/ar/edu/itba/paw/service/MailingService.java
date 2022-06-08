package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Review;
import org.springframework.scheduling.annotation.Async;

import java.math.BigDecimal;
import java.util.Locale;

public interface MailingService {

    @Async
    void sendOfferMail(String bidderEmail, String sellerMail, String nftName, int nftId, String nftAddress, BigDecimal nftPrice, byte[] image, Locale locale);

    @Async
    void sendRegisterMail(String userEmail, String username, Locale locale);
    @Async
    void sendOfferAcceptedMail(String bidderEmail, String sellerMail, int sellerId, String username, String nftName, int nftId, String nftAddress,BigDecimal offerPrice, byte[] image, Locale locale);
    @Async
    void sendOfferRejectedMail(String bidderEmail, String sellerMail, String bidderUsername, String nftName, int nftId, String nftAddress,BigDecimal offerPrice, byte[] image, Locale locale);
    @Async
    public void sendNftCreatedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, byte[] imageBytes, Locale locale);
    @Async
    void sendNftSellOrderCreatedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, BigDecimal nftPrice, byte[] imageBytes, Locale locale);
    @Async
    void sendNftDeletedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, byte[] imageBytes, Locale locale);
    @Async
    void sendSellOrderDeletedMail(String userEmail, String username, int nftId, String nftName, String nftAddress, byte[] imageBytes, BigDecimal nftPrice, Locale locale);

    @Async
    void sendNewReviewMail(String revieweeName, String revieweeEmail, int revieweeUserId, String reviewerName, String reviewerEmail, int score, String reviewTitle, String reviewComment, Locale locale);


}
