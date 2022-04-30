package ar.edu.itba.paw.model;

import java.math.BigDecimal;

public class SellOrder {

    private long id;
    private String category;
    private BigDecimal price;
    private long nft_id;

    public SellOrder(long id, BigDecimal price, long nft_id, String category) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.nft_id = nft_id;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price != null ? price.stripTrailingZeros():null;
    }

    public String getCategory() {
        return category;
    }

    public long getNft_id() {
        return nft_id;
    }

}
