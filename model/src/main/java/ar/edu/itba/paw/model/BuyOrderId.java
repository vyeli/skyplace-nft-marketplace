package ar.edu.itba.paw.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BuyOrderId implements Serializable {
    private final static long serialVersionUID = -8403573375466497833L;
    private int sellOrderId;
    private int buyerId;
    public BuyOrderId() {

    }
    public BuyOrderId(int sellOrderId, int buyerId) {
        this.sellOrderId = sellOrderId;
        this.buyerId = buyerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyOrderId that = (BuyOrderId) o;
        return sellOrderId == that.sellOrderId && buyerId == that.buyerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellOrderId, buyerId);
    }
}
