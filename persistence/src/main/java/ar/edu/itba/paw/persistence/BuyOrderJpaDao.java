package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BuyOrderJpaDao implements BuyOrderDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public BuyOrder create(SellOrder sellOrder, BigDecimal price, User bidder) {
        for(BuyOrder buyorder:bidder.getBuyOrders()) {
            if (buyorder.getOfferedFor().getId() == sellOrder.getId()) {
                buyorder.setAmount(price);
                return buyorder;
            }
        }
        final BuyOrder buyOrder = new BuyOrder(price, sellOrder, bidder);
        em.persist(buyOrder);
        return buyOrder;
    }

    @Override
    public List<BuyOrder> getOrdersBySellOrderId(int page, int sellOrderId, int pageSize) {
        final Query idQuery = em.createNativeQuery("SELECT id_buyer FROM buyorders WHERE id_sellorder=:sellOrderId LIMIT :pageSize OFFSET :pageOffset");
        idQuery.setParameter("pageSize", pageSize);
        idQuery.setParameter("pageOffset", (page-1)*pageSize);
        idQuery.setParameter("sellOrderId", sellOrderId);
        @SuppressWarnings("unchecked")
        final List<Integer> buyerIds = (List<Integer>) idQuery.getResultList().stream().collect(Collectors.toList());

        if(buyerIds.size() == 0)
            return Collections.emptyList();

        final TypedQuery<BuyOrder> query = em.createQuery("FROM BuyOrder AS b WHERE b.offeredFor.id=:sellOrderId AND b.offeredBy.id IN :buyerIds", BuyOrder.class);
        query.setParameter("sellOrderId", sellOrderId);
        query.setParameter("buyerIds",buyerIds);
        return query.getResultList();
    }

}
