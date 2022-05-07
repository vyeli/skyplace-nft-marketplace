package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.SellOrder;

import java.math.BigDecimal;
import java.util.Optional;

public interface SellOrderDao {

    Optional<SellOrder> create(BigDecimal price, String idNft, String category);

    Optional<SellOrder> getOrderById(int id);

    boolean update(int id, String category, BigDecimal price);

    boolean delete(int id);

    int getNftWithOrder(String id);

}
