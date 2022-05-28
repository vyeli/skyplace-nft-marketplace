package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "favorited")
public class Favorited {

    @EmbeddedId
    private FavoritedId favoritedId;
    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @MapsId("nftId")
    @JoinColumn(name = "id_nft", referencedColumnName = "id", nullable = false)
    private Nft nft;

    public Favorited(User user, Nft nft) {
        this.favoritedId = new FavoritedId(user.getId(), nft.getId());
        this.user = user;
        this.nft = nft;
    }

    /* default */ Favorited() {
        // just for hibernate
    }

    public User getUser() {
        return user;
    }

    public Nft getNft() {
        return nft;
    }

}
