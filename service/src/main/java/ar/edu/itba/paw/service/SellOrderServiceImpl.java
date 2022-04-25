package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public Optional<SellOrder> getOrderById(long id) {
        return sellOrderDao.getOrderById(id);
    }

    @Override
    public SellOrder create(String name, int nftId, String nftContract, String chain, String category, BigDecimal price, String description, MultipartFile image, String email) {
        return sellOrderDao.create(name, nftId, nftContract, chain, category, price, description, image, email);
    }

    @Override
    public boolean update(long id, String category, BigDecimal price, String description) {
        // TODO: check if current user is owner
        return sellOrderDao.update(id, category, price, description);
    }

    @Override
    public boolean delete(long id) {
        // TODO: check if current user is owner
        return sellOrderDao.delete(id);
    }
}