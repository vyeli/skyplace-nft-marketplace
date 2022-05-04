package ar.edu.itba.paw.model;

public class BuyOffer {
    private BuyOrder buyOrder;
    private User buyer;

    public BuyOffer(BuyOrder buyOrder, User buyer) {
        this.buyOrder = buyOrder;
        this.buyer = buyer;
    }

    public BuyOrder getBuyOrder() {
        return buyOrder;
    }

    public User getBuyer() {
        return buyer;
    }
}
