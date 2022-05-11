package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;

public interface FavoriteService {
    void addNftFavorite(int productId, User user);

    void removeNftFavorite(int productId, User user);

    int getNftFavorites(int productId);

    boolean userFavedNft(int userId, int nftId);
}
