package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BuyOrderDao {
    boolean create(long id_sellorder, BigDecimal price, long user_id);

    Optional<List<BuyOrder>> getOrdersBySellOrderId(String offerPage, long id_sellorder);

    long getAmountPagesBySellOrderId(long id_sellorder);

    void deleteBuyOrder(String sellOrder, String buyer);
}
