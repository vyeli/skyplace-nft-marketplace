package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderService {

    boolean create(int sellOrderId, BigDecimal price, int userId);

    List<BuyOrder> getOrdersBySellOrderId(int offerPage, SellOrder sellOrder);

    int getAmountPagesBySellOrderId(SellOrder sellOrder);

    List<BuyOrder> getBuyOrdersForUser(User user, int page);

    int getAmountPagesForUser(User user);

    void confirmBuyOrder(int sellOrderId, int buyerId, int seller, int productId, BigDecimal price);

    void deleteBuyOrder(int sellOrderId, int buyerId);
}
