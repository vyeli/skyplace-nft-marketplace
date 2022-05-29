package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuyOrderServiceImpl implements BuyOrderService {
    private final BuyOrderDao buyOrderDao;
    private final UserService userService;
    private final SellOrderService sellOrderService;
    private final ImageService imageService;
    private final MailingService mailingService;
    private final PurchaseService purchaseService;

    private final int pageSize = 5;

    @Autowired
    public BuyOrderServiceImpl(BuyOrderDao buyOrderDao, UserService userService, SellOrderService sellOrderService, ImageService imageService, MailingService mailingService, PurchaseService purchaseService) {
        this.buyOrderDao = buyOrderDao;
        this.userService = userService;
        this.sellOrderService = sellOrderService;
        this.imageService = imageService;
        this.mailingService = mailingService;
        this.purchaseService = purchaseService;
    }

    @Transactional
    @Override
    public boolean create(int idSellOrder, BigDecimal price, int userId) {
        if(sellOrderService.currentUserOwnsSellOrder(idSellOrder))
            throw new UserNoPermissionException();
        
        SellOrder sellOrder = sellOrderService.getOrderById(idSellOrder).orElseThrow(SellOrderNotFoundException::new);
        User bidder = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        buyOrderDao.create(sellOrder, price, bidder);
        Nft nft = sellOrder.getNft();
        User seller = userService.getUserById(nft.getOwner().getId()).orElseThrow(UserNotFoundException::new);
        Image image = imageService.getImage(nft.getIdImage()).orElseThrow(ImageNotFoundException::new);
        mailingService.sendOfferMail(bidder.getEmail(), seller.getEmail(), nft.getNftName(), nft.getId(), nft.getContractAddr(), price, image.getImage(), LocaleContextHolder.getLocale());
        return true;
    }

    @Override
    public List<BuyOffer> getOrdersBySellOrderId(int offerPage, int idSellOrder) {
        // TODO: refactor with SellOrder model getter
        List<BuyOrder> buyOrders = buyOrderDao.getOrdersBySellOrderId(offerPage, idSellOrder, pageSize);
        List<BuyOffer> buyOffers = new ArrayList<>();
        buyOrders.forEach(buyOrder -> {
            Optional<User> user = userService.getUserById(buyOrder.getOfferedBy().getId());
            user.ifPresent(value -> buyOffers.add(new BuyOffer(buyOrder, value)));
        });
        return buyOffers;
    }

    @Override
    public int getAmountPagesBySellOrderId(int idSellOrder) {
        Optional<SellOrder> maybeSellOrder = sellOrderService.getOrderById(idSellOrder);
        return maybeSellOrder.map(sellOrder -> (sellOrder.getBuyOrdersAmount() - 1) / pageSize + 1).orElse(0);
    }

    @Override
    public List<BuyOrder> getBuyOrdersForUser(User user, int page) {
        return user.getBuyOrdersByPage(page, pageSize);
    }

    @Override
    public int getAmountPagesForUser(User user) {
        return (user.getBuyOrders().size() - 1) / pageSize + 1;
    }

    @Transactional
    @Override
    public void confirmBuyOrder(int sellOrderId, int buyerId, int seller, int productId, BigDecimal price) {
        if (!userService.currentUserOwnsNft(productId))
            throw new UserIsNotNftOwnerException();

        SellOrder sellOrder = sellOrderService.getOrderById(sellOrderId).orElseThrow(SellOrderNotFoundException::new);
        User buyerUser = userService.getUserById(buyerId).orElseThrow(UserNotFoundException::new);

        sellOrder.getNft().setOwner(buyerUser);
        sellOrderService.delete(sellOrder.getId());
        purchaseService.createPurchase(buyerId, seller, productId, price);
    }

    @Transactional
    @Override
    public void deleteBuyOrder(int sellOrderId, int buyerId) {
        if (!sellOrderService.currentUserOwnsSellOrder(sellOrderId))
            throw new UserIsNotNftOwnerException();

        // TODO: delete using SellOrder model method
        SellOrder sellOrder = sellOrderService.getOrderById(sellOrderId).orElseThrow(SellOrderNotFoundException::new);
        User buyer = userService.getUserById(buyerId).orElseThrow(UserNotFoundException::new);
        sellOrder.deleteBuyOrder(buyer);
    }



    public int getPageSize() {
        return pageSize;
    }
}
