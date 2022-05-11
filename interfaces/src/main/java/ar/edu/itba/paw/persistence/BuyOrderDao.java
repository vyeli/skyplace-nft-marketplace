package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderDao {
    boolean create(int sellOrderId, BigDecimal price, int userId);

    List<BuyOrder> getOrdersBySellOrderId(int offerPage, int sellOrderId, int pageSize);

    int getAmountPagesBySellOrderId(int sellOrderId, int pageSize);

    void deleteBuyOrder(int sellOrderId, int buyerId);
}
