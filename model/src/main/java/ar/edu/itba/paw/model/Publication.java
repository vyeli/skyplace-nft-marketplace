package ar.edu.itba.paw.model;

public class Publication {
    private Nft nft;
    private SellOrder sellOrder;
    private User user;
    private boolean isFaved = false;

    public Publication(Nft nft, SellOrder sellOrder, User user, boolean isFaved) {
        this.nft = nft;
        this.sellOrder = sellOrder;
        this.user = user;
        this.isFaved = isFaved;
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

    public boolean getIsFaved() {return isFaved;}
}
