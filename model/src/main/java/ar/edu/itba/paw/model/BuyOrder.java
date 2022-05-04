package ar.edu.itba.paw.model;

import java.math.BigDecimal;

public class BuyOrder {
    private long id_sellorder;
    private BigDecimal amount;
    private long id_buyer;

    public BuyOrder(long id_sellorder, BigDecimal amount, long id_buyer) {
        this.id_sellorder = id_sellorder;
        this.amount = amount;
        this.id_buyer = id_buyer;
    }

    public long getId_sellorder() {
        return id_sellorder;
    }

    public BigDecimal getAmount() {
        if(amount == null)
            return amount;
        return new BigDecimal(amount.stripTrailingZeros().toPlainString());
    }

    public long getId_buyer() {
        return id_buyer;
    }
}
