package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserIsNotNftOwnerException;
import ar.edu.itba.paw.exceptions.UserNoPermissionException;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SellOrderServiceImpl implements SellOrderService {

    private final SellOrderDao sellOrderDao;
    private final UserService userService;

    @Autowired
    public SellOrderServiceImpl(SellOrderDao sellOrderDao, UserService userService) {
        this.sellOrderDao = sellOrderDao;
        this.userService = userService;
    }

    @Override
    public Optional<SellOrder> create(BigDecimal price, int idNft, String category) {
        if (!userService.currentUserOwnsNft(idNft))
            throw new UserIsNotNftOwnerException();
        return sellOrderDao.create(price, idNft, category);
    }

    @Override
    public Optional<SellOrder> getOrderById(int id) {
        return sellOrderDao.getOrderById(id);
    }

    @Override
    public boolean update(int id, String category, BigDecimal price) {
        if (!userService.currentUserOwnsNft(id))
            throw new UserNoPermissionException();

        return sellOrderDao.update(id, category, price);
    }

    @Override
    public void delete(int id) {
        if (!userService.currentUserOwnsNft(id) && !userService.isAdmin())
            throw new UserNoPermissionException();

        sellOrderDao.delete(id);
    }

    @Override
    public int getNftWithOrder(int id) {
        return sellOrderDao.getNftWithOrder(id);
    }

}