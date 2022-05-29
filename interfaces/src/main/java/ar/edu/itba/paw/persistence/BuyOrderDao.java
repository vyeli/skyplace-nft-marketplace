package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface BuyOrderDao {

    BuyOrder create(SellOrder sellOrder, BigDecimal price, User bidder);

}
