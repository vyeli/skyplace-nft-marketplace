package ar.edu.itba.paw.model;

public class Publication {
    private Nft nft;
    private SellOrder sellOrder;
    private User user;

    public Publication(Nft nft, SellOrder sellOrder, User user) {
        this.nft = nft;
        this.sellOrder = sellOrder;
        this.user = user;
    }

    public Nft getNft() {
        return nft;
    }

    public SellOrder getSellOrder() {
        return sellOrder;
    }

    public User getUser() {
        return user;
    }
}
