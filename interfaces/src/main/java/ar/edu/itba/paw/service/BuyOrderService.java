package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOffer;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderService {

    boolean create(int sellOrderId, BigDecimal price, int userId);

    List<BuyOffer> getOrdersBySellOrderId(int offerPage, int sellOrderId);

    int getAmountPagesBySellOrderId(int sellOrderId);

    void confirmBuyOrder(int sellOrderId, int buyerId, int seller, int productId, BigDecimal price);

    void deleteBuyOrder(int sellOrderId, int buyerId);
}
