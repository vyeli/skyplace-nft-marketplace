package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellOrderServiceImpl implements SellOrderService {

    private SellOrderDao sellOrderDao;

    @Autowired
    public SellOrderServiceImpl(SellOrderDao sellOrderDao) {
        this.sellOrderDao = sellOrderDao;
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return Optional.empty();
    }

    @Override
    public SellOrder create(String name, double price, String description, byte[] image, String email) {
        return sellOrderDao.create(name, price, description, image, email);
    }
}