package ar.edu.itba.paw.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Purchase {

    private final int id;
    private final User buyer;
    private final User seller;
    private final Nft nft;
    private final BigDecimal price;
    private final Timestamp date;

    public Purchase(int id, User buyer, User seller, Nft nft, BigDecimal price, Timestamp date) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.nft = nft;
        this.price = price;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public Nft getNft() {
        return nft;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Timestamp getDate() {
        return date;
    }
}
