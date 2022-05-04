package ar.edu.itba.paw.model;

import java.math.BigDecimal;

public class BuyOrder {
    private long idSellOrder;
    private BigDecimal amount;
    private long idBuyer;

    public BuyOrder(long idSellOrder, BigDecimal amount, long idBuyer) {
        this.idSellOrder = idSellOrder;
        this.amount = amount;
        this.idBuyer = idBuyer;
    }

    public long getIdSellOrder() {
        return idSellOrder;
    }

    public BigDecimal getAmount() {
        if (amount == null)
            return amount;
        return new BigDecimal(amount.stripTrailingZeros().toPlainString());
    }

    public long getIdBuyer() {
        return idBuyer;
    }
}
