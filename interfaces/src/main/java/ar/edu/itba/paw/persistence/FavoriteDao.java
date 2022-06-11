package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Favorited;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface FavoriteDao {
    void addNftFavorite(int productId, User user);

    void removeNftFavorite(int productId, User user);

    Optional<Favorited> userFavedNft(int userId, int idNft);

    int getNftFavorites(int productId);
}
