package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

public interface FavoriteDao {
    void addNftFavorite(String productId, User user);

    void removeNftFavorite(String productId, User user);

    boolean userFavedNft(long userId, long idNft);

    long getNftFavorites(String productId);
}
