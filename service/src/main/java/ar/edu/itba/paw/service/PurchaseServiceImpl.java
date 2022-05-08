package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Purchase;
import ar.edu.itba.paw.persistence.PurchaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;
    private final int pageSize = 8;

    @Autowired
    public PurchaseServiceImpl(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public List<Purchase> getUserSales(int userId) {
        return purchaseDao.getUserSales(userId);
    }

    @Override
    public List<Purchase> getUserPurchases(int userId) {
        return purchaseDao.getUserPurchases(userId);
    }

    @Override
    public int getAmountPagesByUserId(int userId) {
        return purchaseDao.getAmountPagesByUserId(userId, pageSize);
    }

    @Override
    public List<Purchase> getAllTransactions(int userId, int page) {
        return purchaseDao.getAllTransactions(userId, page, pageSize);
    }

    @Override
    public int createPurchase(int idBuyer, int idSeller, int idNft, BigDecimal price) {
        return purchaseDao.createPurchase(idBuyer, idSeller, idNft, price);
    }
}
