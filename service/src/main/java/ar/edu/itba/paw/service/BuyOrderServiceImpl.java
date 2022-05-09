package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuyOrderServiceImpl implements BuyOrderService {
    private final BuyOrderDao buyOrderDao;
    private final UserDao userDao;
    private final SellOrderDao sellOrderDao;
    private final NftDao nftDao;
    private final ImageDao imageDao;
    private final MailingService mailingService;
    private final PurchaseService purchaseService;

    @Autowired
    public BuyOrderServiceImpl(BuyOrderDao  buyOrderDao, UserDao userDao, SellOrderDao sellOrderDao, NftDao nftDao, ImageDao imageDao, MailingService mailingService, PurchaseService purchaseService) {
        this.buyOrderDao = buyOrderDao;
        this.userDao = userDao;
        this.sellOrderDao = sellOrderDao;
        this.nftDao = nftDao;
        this.imageDao = imageDao;
        this.mailingService = mailingService;
        this.purchaseService = purchaseService;
    }

    @Override
    public boolean create(int idSellOrder, BigDecimal price, int userId) {
        if(buyOrderDao.create(idSellOrder, price, userId)){
            BuyOrder buyOrder = new BuyOrder(idSellOrder, price, userId);
            SellOrder sellOrder = sellOrderDao.getOrderById(idSellOrder).get();
            Nft nft = nftDao.getNFTById(sellOrder.getNftId()).get();
            User seller = userDao.getUserById(nft.getIdOwner()).get();
            User bidder = userDao.getUserById(userId).get();
            Image image = imageDao.getImage(nft.getIdImage());
            mailingService.sendOfferMail(bidder.getEmail(), seller.getEmail(), nft.getNftName(), nft.getId(), nft.getContractAddr(), buyOrder.getAmount(), image.getImage(), LocaleContextHolder.getLocale());
            return true;
        }
        return false;
    }

    @Override
    public List<BuyOffer> getOrdersBySellOrderId(int offerPage, int idSellOrder) {
        List<BuyOrder> buyOrders = buyOrderDao.getOrdersBySellOrderId(offerPage, idSellOrder);
        List<BuyOffer> buyOffers = new ArrayList<>();
        buyOrders.forEach(buyOrder -> {
            Optional<User> user = userDao.getUserById(buyOrder.getIdBuyer());
            user.ifPresent(value -> buyOffers.add(new BuyOffer(buyOrder, value)));
        });
        return buyOffers;
    }

    @Override
    public int getAmountPagesBySellOrderId(int idSellOrder) {
        return buyOrderDao.getAmountPagesBySellOrderId(idSellOrder);
    }

    @Override
    public void confirmBuyOrder(int sellOrderId, int buyerId, int seller, int productId, BigDecimal price) {
        Optional<SellOrder> sOrder = sellOrderDao.getOrderById(sellOrderId);
        if(!sOrder.isPresent())
            return;

        Optional<User> buyerUser = userDao.getUserById(buyerId);
        if(!buyerUser.isPresent())
            return;
        nftDao.updateOwner(sOrder.get().getNftId(), buyerUser.get().getId());

        sellOrderDao.delete(sOrder.get().getId());

        purchaseService.createPurchase(buyerId, seller, productId, price);
    }

    @Override
    public void deleteBuyOrder(int sellOrderId, int buyerId) {
        buyOrderDao.deleteBuyOrder(sellOrderId, buyerId);
    }
}
