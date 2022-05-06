package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public BuyOrderServiceImpl(BuyOrderDao  buyOrderDao, UserDao userDao, SellOrderDao sellOrderDao, NftDao nftDao, ImageDao imageDao, MailingService mailingService) {
        this.buyOrderDao = buyOrderDao;
        this.userDao = userDao;
        this.sellOrderDao = sellOrderDao;
        this.nftDao = nftDao;
        this.imageDao = imageDao;
        this.mailingService = mailingService;
    }

    @Override
    public boolean create(long idSellOrder, BigDecimal price, long userId) {
        if(buyOrderDao.create(idSellOrder, price, userId)){
            BuyOrder buyOrder = new BuyOrder(idSellOrder, price, userId);
            SellOrder sellOrder = sellOrderDao.getOrderById(idSellOrder).get();
            Nft nft = nftDao.getNFTById(String.valueOf(sellOrder.getNftId())).get();
            User seller = userDao.getUserById(nft.getIdOwner()).get();
            User bidder = userDao.getUserById(userId).get();
            Image image = imageDao.getImage(nft.getIdImage());
            mailingService.sendOfferMail(bidder.getEmail(), seller.getEmail(), nft.getNftName(), nft.getId(), nft.getContractAddr(), buyOrder.getAmount(), image.getImage());
            return true;
        }
        return false;
    }

    @Override
    public List<BuyOffer> getOrdersBySellOrderId(String offerPage, long idSellOrder) {
        List<BuyOrder> buyOrders = buyOrderDao.getOrdersBySellOrderId(offerPage, idSellOrder);
        List<BuyOffer> buyOffers = new ArrayList<>();
        buyOrders.forEach(buyOrder -> {
            Optional<User> user = userDao.getUserById(buyOrder.getIdBuyer());
            user.ifPresent(value -> buyOffers.add(new BuyOffer(buyOrder, value)));
        });
        return buyOffers;
    }

    @Override
    public long getAmountPagesBySellOrderId(long idSellOrder) {
        return buyOrderDao.getAmountPagesBySellOrderId(idSellOrder);
    }

    @Override
    public void confirmBuyOrder(String sellOrder, int buyer) {
        long sellOrderId;
        try {
            sellOrderId = Long.parseLong(sellOrder);
        } catch(Exception e) {
            return;
        }
        Optional<SellOrder> sOrder = sellOrderDao.getOrderById(sellOrderId);
        if(!sOrder.isPresent())
            return;

        Optional<User> buyerUser = userDao.getUserById(buyer);
        if(!buyerUser.isPresent())
            return;
        nftDao.updateOwner(sOrder.get().getNftId(), buyerUser.get().getId());

        sellOrderDao.delete(sOrder.get().getId());
    }

    @Override
    public void deleteBuyOrder(String sellOrder, String buyer) {
        buyOrderDao.deleteBuyOrder(sellOrder, buyer);
    }
}
