package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.SellOrder;

import java.math.BigDecimal;
import java.util.Optional;

public interface SellOrderDao {

    SellOrder create(BigDecimal price, Nft nft, Category category);

    Optional<SellOrder> getOrderById(int id);

    boolean update(int id, Category category, BigDecimal price);

    boolean delete(int id);

    boolean sellOrderHasPendingBuyOrder(int sellOrderId);
}
