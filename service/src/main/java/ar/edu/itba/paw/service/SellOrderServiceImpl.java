package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
public class SellOrderServiceImpl implements SellOrderService {

    private final SellOrderDao sellOrderDao;
    private final UserService userService;
    private final NftService nftService;

    private final MailingService mailingService;

    private final ImageService imageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SellOrderServiceImpl.class);

    @Autowired
    public SellOrderServiceImpl(SellOrderDao sellOrderDao, UserService userService, NftService nftService, MailingService mailingService, ImageService imageService) {
        this.sellOrderDao = sellOrderDao;
        this.userService = userService;
        this.nftService = nftService;
        this.mailingService = mailingService;
        this.imageService = imageService;
    }

    @Transactional
    @Override
    public SellOrder create(BigDecimal price, int idNft, String category) {
        if (!userService.currentUserOwnsNft(idNft))
            throw new UserIsNotNftOwnerException();
        if(Arrays.stream(Category.values()).noneMatch(e -> e.name().equals(category)))
            throw new InvalidCategoryException();

        Nft nft = nftService.getNFTById(idNft).orElseThrow(NftNotFoundException::new);
        if(nft.getSellOrder() != null)
            throw new NftAlreadyHasSellOrderException();
        User owner = nft.getOwner();
        SellOrder sellOrder = sellOrderDao.create(price, nft, Category.valueOf(category));
        Image image = imageService.getImage(nft.getIdImage()).orElseThrow(ImageNotFoundException::new);
        Locale locale = Locale.forLanguageTag(owner.getLocale());
        mailingService.sendNftSellOrderCreatedMail(owner.getEmail(), owner.getUsername(), nft.getNftId(), nft.getNftName(), nft.getContractAddr(), price, image.getImage(), locale, nft.getId());
        return sellOrder;
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
        Optional<SellOrder> sellOrder = getOrderById(id);
        if (!sellOrder.isPresent())
            throw new SellOrderNotFoundException();
        if(sellOrderHasPendingBuyOrder(sellOrder.get().getId()))
            throw new SellOrderHasPendingBuyOrderException();
        return sellOrderDao.update(id, Category.valueOf(category), price);
    }

    @Override
    public boolean sellOrderHasPendingBuyOrder(int sellOrderId) {
        return sellOrderDao.sellOrderHasPendingBuyOrder(sellOrderId);
    }

    protected boolean hasPermission(int sellOrderId) {
        return currentUserOwnsSellOrder(sellOrderId) || userService.isAdmin();
    }

    protected void delete(SellOrder sellOrder) {
        Nft nft = sellOrder.getNft();
        User owner = sellOrder.getNft().getOwner();
        Image image = imageService.getImage(nft.getIdImage()).orElseThrow(ImageNotFoundException::new);

        sellOrderDao.delete(sellOrder.getId());
        Locale locale = Locale.forLanguageTag(owner.getLocale());
        mailingService.sendSellOrderDeletedMail(owner.getEmail(), owner.getUsername(),nft.getNftId(), nft.getNftName(), nft.getContractAddr(), image.getImage(), sellOrder.getPrice(), locale);
    }

    @Transactional
    @Override
    public void delete(int sellOrderId) {
        if (!hasPermission(sellOrderId))
            throw new UserNoPermissionException();
        if(sellOrderHasPendingBuyOrder(sellOrderId))
            throw new SellOrderHasPendingBuyOrderException();
        Optional<SellOrder> sellOrder = getOrderById(sellOrderId);
        if(!sellOrder.isPresent())
            throw new SellOrderNotFoundException();
        delete(sellOrder.get());
    }

    @Transactional
    @Override
    public void delete(int sellOrderId, BuyOrder buyOrder) {
        User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        if(!hasPermission(sellOrderId) && (buyOrder == null || buyOrder.getOfferedBy().getId() != currentUser.getId()))
            throw new UserNoPermissionException();

        getOrderById(sellOrderId).ifPresent(this::delete);
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

}