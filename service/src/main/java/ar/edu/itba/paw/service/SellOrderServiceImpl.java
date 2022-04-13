package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        return Optional.empty();
    }

    @Override
    public SellOrder create(String name, int nftId, String nftContract, String chain, String category, double price, String description, MultipartFile image, String email) {
        return sellOrderDao.create(name, nftId, nftContract, chain, category, price, description, image, email);
    }
}