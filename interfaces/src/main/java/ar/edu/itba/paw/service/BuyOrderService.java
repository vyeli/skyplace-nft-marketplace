package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOffer;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderService {

    boolean create(long idSellOrder, BigDecimal price, long userId);

    List<BuyOffer> getOrdersBySellOrderId(String offerPage, long idSellOrder);

    long getAmountPagesBySellOrderId(long idSellOrder);

    void confirmBuyOrder(String sellOrder, int buyer);

    void deleteBuyOrder(String sellOrder, String buyer);
}
