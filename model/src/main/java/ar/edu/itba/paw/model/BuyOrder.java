package ar.edu.itba.paw.model;

import java.math.BigDecimal;

public class BuyOrder {
    private int idSellOrder;
    private BigDecimal amount;
    private int idBuyer;

    public BuyOrder(int idSellOrder, BigDecimal amount, int idBuyer) {
        this.idSellOrder = idSellOrder;
        this.amount = amount;
        this.idBuyer = idBuyer;
    }

    public int getIdSellOrder() {
        return idSellOrder;
    }

    public BigDecimal getAmount() {
        if (amount == null)
            return amount;
        return new BigDecimal(amount.stripTrailingZeros().toPlainString());
    }

    public int getIdBuyer() {
        return idBuyer;
    }
}
