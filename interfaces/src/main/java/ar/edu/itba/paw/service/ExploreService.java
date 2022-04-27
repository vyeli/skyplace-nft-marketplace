package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.User;

import java.util.List;

public interface ExploreService {

    NftCard getNFTById(String id, User user);

    List<NftCard> getNFTs(int page, User user, String categoryName, String chain, double minPrice, double maxPrice, String sort, String search);
}
