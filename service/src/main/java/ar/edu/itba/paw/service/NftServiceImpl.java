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
import java.util.List;
import java.util.Optional;

@Service
public class NftServiceImpl implements NftService{
    private final NftDao nftDao;

    @Autowired
    public NftServiceImpl(NftDao nftDao) {
        this.nftDao = nftDao;
    }

    @Override
    public Optional<Nft> create(int nftId, String contractAddr, String nftName, String chain, MultipartFile image, int idOwner, String collection, String description, String[] properties) {
        return nftDao.create(nftId, contractAddr, nftName, chain, image, idOwner, collection, description, properties);
    }

    @Override
    public Optional<Nft> getNFTById(int nftId) {
        return nftDao.getNFTById(nftId);
    }

    @Override
    public List<Publication> getAllPublications(String page, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, User currentUser) {
        return nftDao.getAllPublications(page, status, category, chain, minPrice, maxPrice, sort, search, currentUser);
    }

    @Override
    public List<Publication> getAllPublicationsByUser(int page, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale) {
        return nftDao.getAllPublicationsByUser(page, user, currentUser, onlyFaved, onlyOnSale);
    }

    @Override
    public int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String search) {
        return nftDao.getAmountPublications(status, category, chain, minPrice, maxPrice, search);
    }

    @Override
    public boolean userOwnsNft(int productId, User user) {
        return getNFTById(productId).filter(value -> value.getIdOwner() == user.getId()).isPresent();
    }

    @Override
    public void delete(int productId) {
        nftDao.delete(productId);
    }

}