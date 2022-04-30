package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface NftDao {
    Optional<Nft> create(long nft_id, String contract_addr, String nft_name, String chain, MultipartFile image, long id_owner, String collection, String description, String[] properties);

    Optional<Nft> getNFTById(String nft_id);

    Optional<List<Nft>> getAllNFTs(int page, String chain, String search);

    Optional<List<Nft>> getAllNFTsByUser(int page, User user);
}
