package ar.edu.itba.paw.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BuyOrderJdbcDao implements BuyOrderDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertBuyOrder;

    @Autowired
    public BuyOrderJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertBuyOrder = new SimpleJdbcInsert(ds)
                .withTableName("buyorders");
    }

    @Override
    public boolean create(long id_sellorder, BigDecimal price, long user_id) {
        Map<String, Object> buyOrderData = new HashMap<>();

        buyOrderData.put("id_sellorder", id_sellorder);
        buyOrderData.put("amount",price);
        buyOrderData.put("id_buyer", user_id);

        return jdbcInsertBuyOrder.execute(buyOrderData) > 0;
    }
}
