package ar.edu.itba.paw.persistence;

import java.math.BigDecimal;

public interface BuyOrderDao {
    boolean create(long id_sellorder, BigDecimal price, long user_id);
}
