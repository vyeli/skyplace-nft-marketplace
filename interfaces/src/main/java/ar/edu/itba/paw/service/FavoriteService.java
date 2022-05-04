package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {
    void addNftFavorite(String productId, User user);

    void removeNftFavorite(String productId, User user);

    long getNftFavorites(String productId);

    boolean userFavedNft(long userId, long id_nft);
}
