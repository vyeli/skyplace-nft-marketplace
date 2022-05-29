package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOffer;
import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderService {

    boolean create(int sellOrderId, BigDecimal price, int userId);

    List<BuyOffer> getOrdersBySellOrderId(int offerPage, int sellOrderId);

    int getAmountPagesBySellOrderId(int sellOrderId);

    List<BuyOrder> getBuyOrdersForUser(User user, int page);

    int getAmountPagesForUser(User user);

    void confirmBuyOrder(int sellOrderId, int buyerId, int seller, int productId, BigDecimal price);

    void deleteBuyOrder(int sellOrderId, int buyerId);
}
