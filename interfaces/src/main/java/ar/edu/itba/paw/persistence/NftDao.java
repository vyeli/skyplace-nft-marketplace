package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface NftDao {
    Optional<Nft> create(int nftId, String contractAddr, String nftName, String chain, MultipartFile image, int idOwner, String collection, String description, String[] properties);

    Optional<Nft> getNFTById(int nftId);

    List<Publication> getAllPublications(int page, int pageSize, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, User currentUser, String searchFor);

    List<Publication> getAllPublicationsByUser(int page, int pageSize, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale, String sort);

    int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String search, String searchFor);

    int getAmountPublicationPagesByUser(int pageSize, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale);

    void updateOwner(int nftId, int idBuyer);

    void delete(int productId);
}
