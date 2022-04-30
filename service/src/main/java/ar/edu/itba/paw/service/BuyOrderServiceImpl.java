package ar.edu.itba.paw.service;

import ar.edu.itba.paw.persistence.BuyOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BuyOrderServiceImpl implements BuyOrderService {
    private final BuyOrderDao buyOrderDao;

    @Autowired
    public BuyOrderServiceImpl(BuyOrderDao  buyOrderDao) {
        this.buyOrderDao = buyOrderDao;
    }

    @Override
    public boolean create(long id_sellorder, BigDecimal price, long user_id) {
        return buyOrderDao.create(id_sellorder, price, user_id);
    }
}
