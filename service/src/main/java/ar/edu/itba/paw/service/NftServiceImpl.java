package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserIsNotNftOwnerException;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.NftDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class NftServiceImpl implements NftService{
    private final NftDao nftDao;
    private final int pageSize = 12;
    private final UserService userService;
    private final SellOrderService sellOrderService;

    @Autowired
    public NftServiceImpl(NftDao nftDao, UserService userService, SellOrderService sellOrderService) {
        this.nftDao = nftDao;
        this.userService = userService;
        this.sellOrderService = sellOrderService;
    }

    @Transactional
    @Override
    public Optional<Nft> create(int nftId, String contractAddr, String nftName, String chain, MultipartFile image, int idOwner, String collection, String description, String[] properties) {
        return nftDao.create(nftId, contractAddr, nftName, chain, image, idOwner, collection, description, properties);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Optional<Nft> getNFTById(int nftId) {
        return nftDao.getNFTById(nftId);
    }

    @Override
    public List<Publication> getAllPublications(int page, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search) {
        User currentUser = userService.getCurrentUser().orElse(null);
        return nftDao.getAllPublications(page, pageSize, status, category, chain, minPrice, maxPrice, sort, search, currentUser);
    }

    @Override
    public List<Publication> getAllPublicationsByUser(int page, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale, String sort) {
        return nftDao.getAllPublicationsByUser(page, pageSize, user, currentUser, onlyFaved, onlyOnSale, sort);
    }

    @Override
    public int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String search) {
        return nftDao.getAmountPublications(status, category, chain, minPrice, maxPrice, search);
    }

    @Override
    public int getAmountPublicationPagesByUser(User user, User currentUser, boolean onlyFaved, boolean onlyOnSale) {
        return nftDao.getAmountPublicationPagesByUser(pageSize, user, currentUser, onlyFaved, onlyOnSale);
    }

    @Override
    public void delete(int productId) {
        if (!userService.currentUserOwnsNft(productId) && !userService.isAdmin())
            throw new UserIsNotNftOwnerException();

        nftDao.delete(productId);
    }

}