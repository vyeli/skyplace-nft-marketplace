package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface NftService {
    Optional<Nft> create(int nftId, String contractAddr, String nftName, String chain, MultipartFile image, int idOwner, String collection, String description, String[] properties);

    Optional<Nft> getNFTById(int nftId);

    List<Publication> getAllPublications(String page, String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String sort, String search, User currentUser);

    List<Publication> getAllPublicationsByUser(int page, User user, User currentUser, boolean onlyFaved, boolean onlyOnSale);

    int getAmountPublications(String status, String category, String chain, BigDecimal minPrice, BigDecimal maxPrice, String search);

    boolean userOwnsNft(int productId, User user);

    void delete(int productId);
}
