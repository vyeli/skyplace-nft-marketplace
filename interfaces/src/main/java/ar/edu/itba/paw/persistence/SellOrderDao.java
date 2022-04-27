package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SellOrderDao {

    List<NftCard> getUserFavorites(User user);

    List<NftCard> getUserSellOrders(User user);

    Optional<SellOrder> getOrderById(long id);

    SellOrder create(String name, int nftId, String nftContract, String chain, String category, BigDecimal price, String description, MultipartFile image, String email);

    boolean update(long id, String category, BigDecimal price, String description);

    boolean delete(long id);

}
