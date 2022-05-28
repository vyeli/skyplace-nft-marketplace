package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Purchase;
import ar.edu.itba.paw.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseDao {

    List<Purchase> getAllTransactions(int userId, int page, int pageSize);

    Purchase createPurchase(User buyer, User seller, Nft nft, BigDecimal price);

}
