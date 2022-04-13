package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.NftCard;

import java.util.List;

public interface ExploreService {

    NftCard getNFTById(long id);

    List<NftCard> getNFTs(int page, String categoryName, String search);
}
