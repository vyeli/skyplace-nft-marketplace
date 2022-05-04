package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOffer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BuyOrderService {

    boolean create(long idSellOrder, BigDecimal price, long userId);

    Optional<List<BuyOffer>> getOrdersBySellOrderId(String offerPage, long idSellOrder);

    long getAmountPagesBySellOrderId(long idSellOrder);

    void confirmBuyOrder(String sellOrder, String buyer);

    void deleteBuyOrder(String sellOrder, String buyer);
}
