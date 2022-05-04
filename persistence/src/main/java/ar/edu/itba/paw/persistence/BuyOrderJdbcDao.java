package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BuyOrderJdbcDao implements BuyOrderDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertBuyOrder;
    private final long PAGE_SIZE = 5;

    @Autowired
    public BuyOrderJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertBuyOrder = new SimpleJdbcInsert(ds)
                .withTableName("buyorders");
    }

    @Override
    public boolean create(long id_sellorder, BigDecimal price, long user_id) {
        List<BigDecimal> buyorder = jdbcTemplate.query("SELECT amount FROM buyorders WHERE id_sellorder = ? AND id_buyer = ?", new Object[]{id_sellorder, user_id}, (rs, rn) -> rs.getBigDecimal("amount"));

        if(buyorder.size()>0) {
            return jdbcTemplate.update("UPDATE buyorders SET amount=? WHERE id_sellorder = ? AND id_buyer = ?", price, id_sellorder, user_id) == 1;
        } else {
            Map<String, Object> buyOrderData = new HashMap<>();

            buyOrderData.put("id_sellorder", id_sellorder);
            buyOrderData.put("amount", price);
            buyOrderData.put("id_buyer", user_id);

            return jdbcInsertBuyOrder.execute(buyOrderData) > 0;
        }
    }

    @Override
    public Optional<List<BuyOrder>> getOrdersBySellOrderId(String offerPage, long id_sellorder) {
        long page;
        if(offerPage == null)
            page = 1;
        else
            try {
                page = Long.parseLong(offerPage);
            } catch(Exception e) {
                return Optional.empty();
            }
        if(page < 1)
            page = 1;

        List<BuyOrder> res = jdbcTemplate.query("SELECT amount, id_buyer FROM buyorders WHERE id_sellorder = ? ORDER BY amount DESC LIMIT ? OFFSET ?", new Object[]{id_sellorder, PAGE_SIZE, (page-1)*PAGE_SIZE}, (rs, rowNum) -> {
           BigDecimal amount = rs.getBigDecimal("amount");
           long id_buyer = rs.getLong("id_buyer");
           return new BuyOrder(id_sellorder, amount, id_buyer);
        } );

        return Optional.of(res);
    }

    @Override
    public long getAmountPagesBySellOrderId(long id_sellorder) {
        List<Long> res = jdbcTemplate.query("SELECT count(*) FROM buyorders WHERE id_sellorder = ?", new Object[]{id_sellorder}, (rs , rowNum) -> rs.getLong("count"));
        return res.size() > 0 ? res.get(0)/PAGE_SIZE+1:0;
    }

    @Override
    public void deleteBuyOrder(String sellOrder, String buyer) {
        try{
            long sellOrderLong = Long.parseLong(sellOrder);
            long buyerLong = Long.parseLong(buyer);
            jdbcTemplate.update("DELETE FROM buyorders WHERE id_sellorder = ? AND id_buyer = ?", sellOrderLong, buyerLong);
        }catch(Exception ignored){}
    }
}
