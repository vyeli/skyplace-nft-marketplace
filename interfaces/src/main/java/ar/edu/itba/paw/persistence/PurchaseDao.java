package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Purchase;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseDao {

    List<Purchase> getUserSales(int userId);

    List<Purchase> getUserPurchases(int userId);

    List<Purchase> getAllTransactions(int userId, int page, int pageSize);

    int getAmountPagesByUserId(int userId, int pageSize);

    int createPurchase(int idBuyer, int idSeller, int idNft, BigDecimal price);

}
