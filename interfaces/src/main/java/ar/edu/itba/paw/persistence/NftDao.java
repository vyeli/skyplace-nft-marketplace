package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface NftDao {
    Optional<Nft> create(long nftId, String contractAddr, String nftName, String chain, MultipartFile image, long idOwner, String collection, String description, String[] properties);

    Optional<Nft> getNFTById(String nftId);

    List<Nft> getAllNFTs(int page, String chain, String search);

    List<Nft> getAllNFTsByUser(int page, User user);

    void updateOwner(long nftId, long idBuyer);
}
