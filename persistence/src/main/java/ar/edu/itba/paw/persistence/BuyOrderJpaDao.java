package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

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

}
