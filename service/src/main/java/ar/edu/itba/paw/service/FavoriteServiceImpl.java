package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.FavoriteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteDao favoriteDao;

    @Autowired
    public FavoriteServiceImpl(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Override
    public void addNftFavorite(int productId, User user) {
        favoriteDao.addNftFavorite(productId, user);
    }

    @Override
    public void removeNftFavorite(int productId, User user) {
        favoriteDao.removeNftFavorite(productId, user);
    }

    @Override
    public int getNftFavorites(int productId) {
        return favoriteDao.getNftFavorites(productId);
    }

    @Override
    public boolean userFavedNft(int userId, int idNft) {
        return favoriteDao.userFavedNft(userId, idNft);
    }
}
