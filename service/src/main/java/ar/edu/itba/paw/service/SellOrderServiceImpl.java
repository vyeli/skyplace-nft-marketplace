package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Service
public class SellOrderServiceImpl implements SellOrderService {

    private final SellOrderDao sellOrderDao;
    private final UserService userService;
    private final NftService nftService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SellOrderServiceImpl.class);

    @Autowired
    public SellOrderServiceImpl(SellOrderDao sellOrderDao, UserService userService, NftService nftService) {
        this.sellOrderDao = sellOrderDao;
        this.userService = userService;
        this.nftService = nftService;
    }

    @Transactional
    @Override
    public SellOrder create(BigDecimal price, int idNft, String category) {
        if (!userService.currentUserOwnsNft(idNft))
            throw new UserIsNotNftOwnerException();
        if(Arrays.stream(Category.values()).noneMatch(e -> e.name().equals(category)))
            throw new InvalidCategoryException();

        Nft nft = nftService.getNFTById(idNft).orElseThrow(NftNotFoundException::new);
        return sellOrderDao.create(price, nft, Category.valueOf(category));
    }

    @Override
    public Optional<SellOrder> getOrderById(int id) {
        return sellOrderDao.getOrderById(id);
    }

    @Transactional
    @Override
    public boolean update(int id, String category, BigDecimal price) {
        if (!currentUserOwnsSellOrder(id))
            throw new UserNoPermissionException();
        if(Arrays.stream(Category.values()).noneMatch(e -> e.name().equals(category)))
            throw new InvalidCategoryException();
        return sellOrderDao.update(id, Category.valueOf(category), price);
    }

    @Transactional
    @Override
    public void delete(int id) {
        if (!currentUserOwnsSellOrder(id) && !userService.isAdmin())
            throw new UserNoPermissionException();

        sellOrderDao.delete(id);
    }

    @Override
    public boolean userOwnsSellOrder(int sellOrderId, User user) {
        Optional<SellOrder> maybeSellOrder = getOrderById(sellOrderId);
        return maybeSellOrder.filter(sellOrder -> userService.userOwnsNft(sellOrder.getNft().getId(), user)).isPresent();
    }

    @Override
    public boolean currentUserOwnsSellOrder(int productId) {
        User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        return userOwnsSellOrder(productId, currentUser);
    }

    @Override
    public int getNftWithOrder(int id) {
        return sellOrderDao.getNftWithOrder(id);
    }

}