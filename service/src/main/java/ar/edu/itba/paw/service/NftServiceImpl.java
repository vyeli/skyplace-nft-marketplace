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
    public Optional<List<Publication>> getAllPublications(int page, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, User currentUser) {
        Optional<List<Nft>> nftsOptional = nftDao.getAllNFTs(page, chain, search);
        if(!nftsOptional.isPresent())
            return Optional.empty();
        List<Nft> nfts = nftsOptional.get();
        List<Publication> publications = new ArrayList<>();
        nfts.forEach(nft -> {
            Optional<User> user = userDao.getUserById(nft.getIdOwner());
            if(!user.isPresent())
                return;
            Optional<SellOrder> sellOrder = Optional.empty();
            if(nft.getSellOrder() != null)
                sellOrder = sellOrderDao.getOrderById(nft.getSellOrder());
            boolean isFaved = false;
            if(currentUser != null)
                isFaved = favoriteDao.userFavedNft(currentUser.getId(), nft.getId());

            publications.add(new Publication(nft, sellOrder.orElse(null), user.get(), isFaved));
        });

        return Optional.of(publications);
    }

    @Override
    public Optional<List<Publication>> getAllPublicationsByUser(int page, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale) {
        Optional<List<Nft>> nftsOptional = nftDao.getAllNFTsByUser(page, user);
        if(!nftsOptional.isPresent())
            return Optional.empty();
        List<Nft> nfts = nftsOptional.get();
        List<Publication> publications = new ArrayList<>();
        nfts.forEach(nft -> {
            if(onlyFaved && !favoriteDao.userFavedNft(user.getId(), nft.getId()))
                return;

            Optional<SellOrder> sellOrder = Optional.empty();
            if(nft.getSellOrder() != null)
                sellOrder = sellOrderDao.getOrderById(nft.getSellOrder());

            if(onlyOnSale && !sellOrder.isPresent())
                return;

            boolean isFaved = false;
            if(currentUser != null)
                isFaved = favoriteDao.userFavedNft(currentUser.getId(), nft.getId());

            publications.add(new Publication(nft, sellOrder.orElse(null), user, isFaved));
        });

        return Optional.of(publications);
    }

    @Override
    public boolean userOwnsNft(String productId, User user) {
        return getNFTById(productId).filter(value -> value.getIdOwner() == user.getId()).isPresent();
    }

}