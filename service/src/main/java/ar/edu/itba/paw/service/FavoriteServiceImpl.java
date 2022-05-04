package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.FavoriteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService{
    private final FavoriteDao favoriteDao;

    @Autowired
    public FavoriteServiceImpl(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @Override
    public void addNftFavorite(String productId, User user) {
        favoriteDao.addNftFavorite(productId, user);
    }

    @Override
    public void removeNftFavorite(String productId, User user) {
        favoriteDao.removeNftFavorite(productId, user);
    }

    @Override
    public long getNftFavorites(String productId) {
        return favoriteDao.getNftFavorites(productId);
    }

    @Override
    public boolean userFavedNft(long userId, long idNft) {
        return favoriteDao.userFavedNft(userId, idNft);
    }
}
