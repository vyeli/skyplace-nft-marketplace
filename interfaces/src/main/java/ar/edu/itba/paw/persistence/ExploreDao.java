package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.NftCard;

import java.util.List;

public interface ExploreDao {

        NftCard getNFTById(long id);

        List<NftCard> getNFTs(int page, String categoryName, String search);
}
