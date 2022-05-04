package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BuyOrderDao {
    boolean create(long idSellOrder, BigDecimal price, long userId);

    Optional<List<BuyOrder>> getOrdersBySellOrderId(String offerPage, long idSellOrder);

    long getAmountPagesBySellOrderId(long idSellOrder);

    void deleteBuyOrder(String sellOrder, String buyer);
}
