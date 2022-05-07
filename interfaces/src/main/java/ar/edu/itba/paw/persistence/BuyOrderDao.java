package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderDao {
    boolean create(int idSellOrder, BigDecimal price, int userId);

    List<BuyOrder> getOrdersBySellOrderId(String offerPage, int idSellOrder);

    int getAmountPagesBySellOrderId(int idSellOrder);

    void deleteBuyOrder(String sellOrder, String buyer);
}
