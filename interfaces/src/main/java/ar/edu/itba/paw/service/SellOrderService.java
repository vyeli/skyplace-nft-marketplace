package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SellOrderService {

    List<NftCard> getUserFavorites(User user);

    Optional<SellOrder> getOrderById(long id);

    List<NftCard> getUserSellOrders(User user);

    SellOrder create(String name, int nftId, String nftContract, String chain, String category, BigDecimal price, String description, MultipartFile image);

    boolean update(long id, String category, BigDecimal price, String description);

    boolean delete(long id);

    boolean isUserOwner(long id);

}
