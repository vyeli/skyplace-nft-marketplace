package ar.edu.itba.paw.service;

public interface MailingService {
    boolean sendMail(String buyerEmail, String sellerMail, String nftName, String nftAddress, float nftPrice);
}
