package ar.edu.itba.paw.model;

import java.math.BigDecimal;

public class SellOrder {

    private int id;
    private String category;
    private BigDecimal price;
    private int nftId;

    public SellOrder(int id, BigDecimal price, int nftId, String category) {
        this.id = id;
        this.price = price;
        this.category = category;
        this.nftId = nftId;
    }

    public int getId() {
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

    public int getNftId() {
        return nftId;
    }

}
