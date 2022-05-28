package ar.edu.itba.paw.model;

public class Publication {
    private Nft nft;
    private Favorited favorite;

    public Publication(Nft nft, Favorited favorite) {
        this.nft = nft;
        this.favorite = favorite;
    }

    public Nft getNft() {
        return nft;
    }

    public Favorited getIsFaved() {return favorite;}
}
