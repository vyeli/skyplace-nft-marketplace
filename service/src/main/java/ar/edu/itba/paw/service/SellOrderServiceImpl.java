package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SellOrderServiceImpl implements SellOrderService {

    private final SellOrderDao sellOrderDao;

    @Autowired
    public SellOrderServiceImpl(SellOrderDao sellOrderDao) {
        this.sellOrderDao = sellOrderDao;
    }

    @Override
    public Optional<SellOrder> create(BigDecimal price, String idNft, String category) {
        return sellOrderDao.create(price, idNft, category);
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return sellOrderDao.getOrderById(id);
    }

    @Override
    public void update(long id, String category, BigDecimal price) {
        sellOrderDao.update(id, category, price);
    }

    @Override
    public void delete(long id) {
        sellOrderDao.delete(id);
    }

    @Override
    public long getNftWithOrder(String id) {
        return sellOrderDao.getNftWithOrder(id);
    }
}