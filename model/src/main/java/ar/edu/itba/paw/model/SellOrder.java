package ar.edu.itba.paw.model;

import java.math.BigDecimal;

public class SellOrder {

    private long id;
    private String sellerEmail;
    private String description;
    private String category;
    private BigDecimal price;
    private int nftId;
    private String nftAddress;

    public SellOrder(long id, String sellerEmail, String description, BigDecimal price, int nftId, String nftAddress, String category) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.description = description;
        this.price = price;
        this.nftId = nftId;
        this.nftAddress = nftAddress;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getNftId() {
        return nftId;
    }

    public String getNftAddress() {
        return nftAddress;
    }

    public String getCategory() {
        return category;
    }

}
