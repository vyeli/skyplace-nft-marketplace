package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.SellOrder;

import java.math.BigDecimal;
import java.util.Optional;

public interface SellOrderService {

    Optional<SellOrder> create(BigDecimal price, String idNft, String category);

    Optional<SellOrder> getOrderById(int id);

    void update(int id, String category, BigDecimal price);

    void delete(int id);

    int getNftWithOrder(String id);

}
