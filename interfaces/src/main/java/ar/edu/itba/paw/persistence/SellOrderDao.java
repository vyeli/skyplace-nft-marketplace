package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SellOrderDao {

    boolean addFavorite(long userId, long sellOrderId);

    boolean removeFavorite(long userId, long sellOrderId);

    List<NftCard> getUserFavorites(long userId);

    List<NftCard> getUserSellOrders(String userEmail);

    Optional<SellOrder> getOrderById(long id);

    boolean update(long id, String category, BigDecimal price, String description);

    boolean delete(long id);

}
