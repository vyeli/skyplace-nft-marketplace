package ar.edu.itba.paw.service;

import ar.edu.itba.paw.persistence.ChainDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChainServiceImpl implements ChainService {

    private final ChainDao chainDao;

    @Autowired
    public ChainServiceImpl(ChainDao categoryDao) {
        this.chainDao = categoryDao;
    }

    @Override
    public List<String> getChains() {
        return chainDao.getChains();
    }
}
