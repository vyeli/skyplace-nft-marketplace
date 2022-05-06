package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Purchase;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseService {

    List<Purchase> getUserSales(int userId);

    List<Purchase> getUserPurchases(int userId);

    List<Purchase> getAllTransactions(int userId);

    int createPurchase(int idBuyer, int idSeller, int idNft, BigDecimal price);

}
