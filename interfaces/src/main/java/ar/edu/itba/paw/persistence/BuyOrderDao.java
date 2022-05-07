package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderDao {
    boolean create(int sellOrderId, BigDecimal price, int userId);

    List<BuyOrder> getOrdersBySellOrderId(Integer offerPage, int sellOrderId);

    int getAmountPagesBySellOrderId(int sellOrderId);

    void deleteBuyOrder(int sellOrderId, int buyerId);
}
