package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.SellOrder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class SellOrderJpaDao implements SellOrderDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public SellOrder create(BigDecimal price, Nft nft, Category category) {
        final SellOrder sellOrder = new SellOrder(price, nft, category);
        em.persist(sellOrder);
        return sellOrder;
    }

    @Override
    public Optional<SellOrder> getOrderById(int id) {
        final TypedQuery<SellOrder> query = em.createQuery("from SellOrder sellorder where sellorder.id = :id", SellOrder.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public boolean update(int id, Category category, BigDecimal price) {
        final Query query = em.createQuery("update SellOrder sellorder set sellorder.category = :category, sellorder.price = :price where sellorder.id = :id");
        query.setParameter("id", id);
        query.setParameter("category", category);
        query.setParameter("price", price);
        return query.executeUpdate() == 1;
    }

    @Override
    public boolean delete(int id) {
        final Query query = em.createQuery("delete from SellOrder sellorder where sellorder.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate() == 1;
    }

    @Override
    public int getNftWithOrder(int id) {
        final Query query = em.createQuery("select sellorder.idNft from SellOrder sellorder where sellorder.id = :id");
        query.setParameter("id", id);
        Optional<Integer> nftId;
        nftId = (Optional<Integer>) query.getResultList().stream().findFirst();  // FIXME: hacer que en la interface devuelva un Optional de integer ?
        return nftId.orElse(-1);
    }
}
