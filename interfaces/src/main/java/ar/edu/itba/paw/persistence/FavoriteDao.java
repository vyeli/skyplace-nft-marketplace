package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

public interface FavoriteDao {
    void addNftFavorite(String productId, User user);

    void removeNftFavorite(String productId, User user);

    boolean userFavedNft(int userId, int idNft);

    int getNftFavorites(String productId);
}
