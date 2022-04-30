package ar.edu.itba.paw.service;

import java.math.BigDecimal;

public interface BuyOrderService {

    boolean create(long id_sellorder, BigDecimal price, long user_id);
}
