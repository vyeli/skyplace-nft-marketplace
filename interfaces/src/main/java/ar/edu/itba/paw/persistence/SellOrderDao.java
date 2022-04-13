package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.SellOrder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface SellOrderDao {

    Optional<SellOrder> getOrderById(long id);

    SellOrder create(String name, int nftId, String nftContract, String chain, String category, double price, String description, MultipartFile image, String email);

}
