package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.FavoriteDao;
import ar.edu.itba.paw.persistence.NftDao;
import ar.edu.itba.paw.persistence.SellOrderDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class NftServiceImpl implements NftService{
    private final NftDao nftDao;
    private final SellOrderDao sellOrderDao;
    private final UserDao userDao;
    private final FavoriteDao favoriteDao;

    @Autowired
    public NftServiceImpl(NftDao nftDao, SellOrderDao sellOrderDao, UserDao userDao, FavoriteDao favoriteDao) {
        this.nftDao = nftDao;
        this.sellOrderDao = sellOrderDao;
        this.userDao = userDao;
        this.favoriteDao = favoriteDao;
    }

    @Override
    public Optional<Nft> create(long nftId, String contractAddr, String nftName, String chain, MultipartFile image, long idOwner, String collection, String description, String[] properties) {
        return nftDao.create(nftId, contractAddr, nftName, chain, image, idOwner, collection, description, properties);
    }

    @Override
    public Optional<Nft> getNFTById(String nftId) {
        return nftDao.getNFTById(nftId);
    }

    @Override
    public List<Publication> getAllPublications(int page, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, User currentUser) {
        return nftDao.getAllPublications(page, category, chain, minPrice, maxPrice, sort, search, currentUser);
    }

    @Override
    public List<Publication> getAllPublicationsByUser(int page, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale) {
        return nftDao.getAllPublicationsByUser(page, user, currentUser, onlyFaved, onlyOnSale);
    }

    @Override
    public boolean userOwnsNft(String productId, User user) {
        return getNFTById(productId).filter(value -> value.getIdOwner() == user.getId()).isPresent();
    }

    @Override
    public void delete(String productId) {
        nftDao.delete(productId);
    }

}