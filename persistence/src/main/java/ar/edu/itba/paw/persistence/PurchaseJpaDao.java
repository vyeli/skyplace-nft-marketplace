package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Purchase;
import ar.edu.itba.paw.model.StatusPurchase;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PurchaseJpaDao implements PurchaseDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Purchase> getAllTransactions(int userId, int page, int pageSize) {
        final Query idQuery = em.createNativeQuery("SELECT id FROM purchases WHERE id_buyer = :userId OR id_seller = :userId ORDER BY buy_date DESC LIMIT :pageSize OFFSET :offset");
        idQuery.setParameter("userId", userId);
        idQuery.setParameter("pageSize", pageSize);
        idQuery.setParameter("offset", pageSize * (page - 1));
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) idQuery.getResultList().stream().collect(Collectors.toList());

        if(ids.size() == 0)
            return new ArrayList<>();

        final TypedQuery<Purchase> query = em.createQuery("from Purchase WHERE id IN :ids ORDER BY buyDate DESC", Purchase.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Purchase createPurchase(User buyer, User seller, Nft nft, BigDecimal price, String txHash, StatusPurchase statusPurchase) {
        Timestamp currentTime = new Timestamp(Instant.now().toEpochMilli());
        currentTime.setNanos(0);
        final Purchase newPurchase = new Purchase(price, currentTime, nft, buyer, seller, statusPurchase, txHash);
        em.persist(newPurchase);
        return newPurchase;
    }

    @Override
    public boolean isTxHashAlreadyInUse(String txHash) {
        final TypedQuery<Purchase> query = em.createQuery("FROM Purchase AS p WHERE p.status = :statusPurchase AND p.txHash = :txHash",Purchase.class);
        query.setParameter("statusPurchase", StatusPurchase.SUCCESS);
        query.setParameter("txHash", txHash);
        return query.getResultList().stream().findFirst().isPresent();
    }
}
