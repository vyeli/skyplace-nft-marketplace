package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SellOrderService {

    boolean addFavorite(long userId, long sellOrderId);

    boolean removeFavorite(long userId, long sellOrderId);

    List<NftCard> getUserFavorites(long userId);

    Optional<SellOrder> getOrderById(long id);

    List<NftCard> getUserSellOrders(String userEmail);

    boolean update(long id, String category, BigDecimal price, String description);

    boolean delete(long id);

    boolean isUserOwner(long id);

}
