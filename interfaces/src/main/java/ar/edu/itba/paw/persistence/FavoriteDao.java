package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

public interface FavoriteDao {
    void addNftFavorite(String productId, User user);

    void removeNftFavorite(String productId, User user);

    boolean userFavedNft(long user_id, long id_nft);

    long getNftFavorites(String productId);
}
