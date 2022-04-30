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
    public Optional<SellOrder> create(BigDecimal price, String id_nft, String category) {
        return sellOrderDao.create(price, id_nft, category);
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return sellOrderDao.getOrderById(id);
    }

    @Override
    public boolean update(long id, String category, BigDecimal price, String description) {
        if (!isUserOwner(id))
            return false;
        return sellOrderDao.update(id, category, price, description);
    }

    @Override
    public boolean delete(long id) {
        if (!isUserOwner(id))
            return false;
        return sellOrderDao.delete(id);
    }

    @Override
    public boolean isUserOwner(long id){
        return true;
    }
}