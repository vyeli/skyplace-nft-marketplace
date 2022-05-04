package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOffer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BuyOrderService {

    boolean create(long id_sellorder, BigDecimal price, long user_id);

    Optional<List<BuyOffer>> getOrdersBySellOrderId(String offerPage, long id_sellorder);

    long getAmountPagesBySellOrderId(long id_sellorder);

    void confirmBuyOrder(String sellOrder, String buyer);

    void deleteBuyOrder(String sellOrder, String buyer);
}
