package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Favorited;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.FavoriteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteDao favoriteDao;

    @Autowired
    public FavoriteServiceImpl(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Transactional
    @Override
    public void addNftFavorite(int productId, User user) {
        if(!isNftFavedByUser(user.getId(), productId))
            favoriteDao.addNftFavorite(productId, user);
    }

    @Transactional
    @Override
    public void removeNftFavorite(int productId, User user) {
        favoriteDao.removeNftFavorite(productId, user);
    }

    @Override
    public int getNftFavorites(int productId) {
        return favoriteDao.getNftFavorites(productId);
    }

    @Override
    public boolean isNftFavedByUser(int userId, int idNft) {
        return userFavedNft(userId, idNft).isPresent();
    }

    @Override
    public Optional<Favorited> userFavedNft(int userId, int idNft) {
        return favoriteDao.userFavedNft(userId, idNft);
    }
}
