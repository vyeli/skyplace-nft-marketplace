package ar.edu.itba.paw.model;

import java.math.BigDecimal;

public class SellOrder {

    private long id;
    private String category;
    private BigDecimal price;
    private long nftId;

    public SellOrder(long id, BigDecimal price, long nftId, String category) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.nftId = nftId;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        if(price == null)
            return price;
        return new BigDecimal(price.stripTrailingZeros().toPlainString());
    }

    public String getCategory() {
        return category;
    }

    public long getNftId() {
        return nftId;
    }

}
