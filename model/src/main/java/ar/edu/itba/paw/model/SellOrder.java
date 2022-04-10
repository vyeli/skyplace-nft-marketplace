package ar.edu.itba.paw.model;

public class SellOrder {

    private long id;
    private String sellerEmail;
    private String description;
    private double price;
    private int nftId;
    private String nftAddress;

    public SellOrder(long id, String sellerEmail, String description, double price, int nftId, String nftAddress) {
        this.id = id;
        this.sellerEmail = sellerEmail;
        this.description = description;
        this.price = price;
        this.nftId = nftId;
        this.nftAddress = nftAddress;
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

    public double getPrice() {
        return price;
    }

    public int getNftId() {
        return nftId;
    }

    public String getNftAddress() {
        return nftAddress;
    }
}
