package ar.edu.itba.paw.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoritedId implements Serializable {
    private final static long serialVersionUID = 7446458900386724875L;

    private int userId;

    private int nftId;

    public FavoritedId() {

    }

    public FavoritedId(int userId, int nftId) {
        this.userId = userId;
        this.nftId = nftId;
    }

    public int getUserId() {
        return userId;
    }

    public int getNftId() {
        return nftId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritedId that = (FavoritedId) o;
        return userId == that.userId && nftId == that.nftId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, nftId);
    }
}
