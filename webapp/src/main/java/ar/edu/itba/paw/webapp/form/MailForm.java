package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class MailForm {
    @NotEmpty
    @Email
    private String buyerMail;

    private String nftName;

    private String nftAddress;

    private float nftPrice;

    private String sellerMail;

    public String getBuyerMail() {
        return buyerMail;
    }

    public void setBuyerMail(String buyerMail) {
        this.buyerMail = buyerMail;
    }

    public String getNftName() {
        return nftName;
    }

    public String getSellerMail() {
        return sellerMail;
    }

    public void setSellerMail(String sellerMail) {
        this.sellerMail = sellerMail;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public String getNftAddress() {
        return nftAddress;
    }

    public void setNftAddress(String nftAddress) {
        this.nftAddress = nftAddress;
    }

    public float getNftPrice() {
        return nftPrice;
    }

    public void setNftPrice(float nftPrice) {
        this.nftPrice = nftPrice;
    }
}
