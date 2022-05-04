package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOffer;
import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.BuyOrderDao;
import ar.edu.itba.paw.persistence.NftDao;
import ar.edu.itba.paw.persistence.SellOrderDao;
import ar.edu.itba.paw.persistence.UserDao;
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
    @Autowired
    public BuyOrderServiceImpl(BuyOrderDao  buyOrderDao, UserDao userDao, SellOrderDao sellOrderDao, NftDao nftDao) {
        this.buyOrderDao = buyOrderDao;
        this.userDao = userDao;
        this.sellOrderDao = sellOrderDao;
        this.nftDao = nftDao;
    }

    @Override
    public boolean create(long idSellOrder, BigDecimal price, long userId) {
        return buyOrderDao.create(idSellOrder, price, userId);
    }

    @Override
    public Optional<List<BuyOffer>> getOrdersBySellOrderId(String offerPage, long idSellOrder) {
        Optional<List<BuyOrder>> buyOrders = buyOrderDao.getOrdersBySellOrderId(offerPage, idSellOrder);
        List<BuyOffer> buyOffers = new ArrayList<>();
        buyOrders.ifPresent(orders -> orders.forEach(buyOrder -> {
            Optional<User> user = userDao.getUserById(buyOrder.getIdBuyer());
            user.ifPresent(value -> buyOffers.add(new BuyOffer(buyOrder, value)));
        }));
        return Optional.of(buyOffers);
    }

    @Override
    public long getAmountPagesBySellOrderId(long idSellOrder) {
        return buyOrderDao.getAmountPagesBySellOrderId(idSellOrder);
    }

    @Override
    public void confirmBuyOrder(String sellOrder, String buyer) {
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
