package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.persistence.ExploreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExploreServiceImpl implements ExploreService{
    private ExploreDao exploreDao;

    @Autowired
    public ExploreServiceImpl(ExploreDao exploreDao) {
        this.exploreDao = exploreDao;
    }

    @Override
    public NftCard getNFTById(String id) {
        return exploreDao.getNFTById(id);
    }

    @Override
    public List<NftCard> getNFTs(int page, String categoryName, String search) {
        return exploreDao.getNFTs(page, categoryName.substring(0,1).toUpperCase()+categoryName.substring(1), search);
    }
}
