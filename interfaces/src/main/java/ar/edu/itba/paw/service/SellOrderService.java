package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.SellOrder;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

public interface SellOrderService {

    Optional<SellOrder> getOrderById(long id);

    SellOrder create(String name, int nftId, String nftContract, String chain, String category, BigDecimal price, String description, MultipartFile image, String email);

    boolean update(long id, String category, BigDecimal price, String description);

    boolean delete(long id);

    boolean isUserOwner(long id);

}
