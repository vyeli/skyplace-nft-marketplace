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
    public Optional<SellOrder> create(BigDecimal price, int idNft, String category) {
        return sellOrderDao.create(price, idNft, category);
    }

    @Override
    public Optional<SellOrder> getOrderById(int id) {
        return sellOrderDao.getOrderById(id);
    }

    @Override
    public boolean update(int id, String category, BigDecimal price) {
        return sellOrderDao.update(id, category, price);
    }

    @Override
    public void delete(int id) {
        sellOrderDao.delete(id);
    }

    @Override
    public int getNftWithOrder(int id) {
        return sellOrderDao.getNftWithOrder(id);
    }

}