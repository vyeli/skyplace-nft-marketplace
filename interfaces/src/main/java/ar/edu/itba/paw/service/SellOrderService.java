package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface SellOrderService {

    SellOrder create(BigDecimal price, int idNft, String category);

    Optional<SellOrder> getOrderById(int id);

    boolean update(int id, String category, BigDecimal price);

    void delete(int id);

    void delete(int sellOrderId, BuyOrder buyOrder);

    boolean userOwnsSellOrder(int productId, User user);

    boolean currentUserOwnsSellOrder(int productId);

}
