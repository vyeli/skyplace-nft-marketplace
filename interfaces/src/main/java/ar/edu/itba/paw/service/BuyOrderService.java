package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOffer;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderService {

    boolean create(int idSellOrder, BigDecimal price, int userId);

    List<BuyOffer> getOrdersBySellOrderId(String offerPage, int idSellOrder);

    int getAmountPagesBySellOrderId(int idSellOrder);

    void confirmBuyOrder(String sellOrder, int buyer);

    void deleteBuyOrder(String sellOrder, String buyer);
}
