package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "buyorders")
public class BuyOrder {

    @EmbeddedId
    private BuyOrderId buyOrderId;

    @JoinColumn(name = "id_sellorder", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId("sellOrderId")
    private SellOrder offeredFor;

    @JoinColumn(name = "id_buyer", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId("buyerId")
    private User offeredBy;

    @Column(name = "amount", nullable = false, precision = 18)
    private BigDecimal amount;

    /* default */ BuyOrder() {
        // just for hibernate
    }

    public BuyOrder(BigDecimal amount, SellOrder offeredFor, User offeredBy) {
        this.amount = amount;
        this.offeredBy = offeredBy;
        this.offeredFor = offeredFor;
        this.buyOrderId = new BuyOrderId(offeredFor.getId(), offeredBy.getId());
    }

    public BigDecimal getAmount() {
        return new BigDecimal(amount.stripTrailingZeros().toPlainString());
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public SellOrder getOfferedFor() {
        return offeredFor;
    }

    public User getOfferedBy() {
        return offeredBy;
    }

    public BuyOrderId getBuyOrderId() {
        return buyOrderId;
    }
}
