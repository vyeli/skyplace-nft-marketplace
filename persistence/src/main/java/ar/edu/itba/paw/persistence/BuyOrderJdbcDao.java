package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class BuyOrderJdbcDao implements BuyOrderDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertBuyOrder;
    private final int PAGE_SIZE = 5;

    @Autowired
    public BuyOrderJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertBuyOrder = new SimpleJdbcInsert(ds)
                .withTableName("buyorders");
    }

    @Override
    public boolean create(int idSellOrder, BigDecimal price, int userId) {
        List<BigDecimal> buyOrder = jdbcTemplate.query("SELECT amount FROM buyorders WHERE id_sellorder = ? AND id_buyer = ?", new Object[]{idSellOrder, userId}, (rs, rn) -> rs.getBigDecimal("amount"));

        if(buyOrder.size()>0) {
            return jdbcTemplate.update("UPDATE buyorders SET amount=? WHERE id_sellorder = ? AND id_buyer = ?", price, idSellOrder, userId) == 1;
        } else {
            Map<String, Object> buyOrderData = new HashMap<>();

            buyOrderData.put("id_sellorder", idSellOrder);
            buyOrderData.put("amount", price);
            buyOrderData.put("id_buyer", userId);

            return jdbcInsertBuyOrder.execute(buyOrderData) > 0;
        }
    }

    @Override
    public List<BuyOrder> getOrdersBySellOrderId(int offerPage, int idSellOrder) {
        return jdbcTemplate.query("SELECT amount, id_buyer FROM buyorders WHERE id_sellorder = ? ORDER BY amount DESC LIMIT ? OFFSET ?", new Object[]{idSellOrder, PAGE_SIZE, (offerPage-1)*PAGE_SIZE}, (rs, rowNum) -> {
           BigDecimal amount = rs.getBigDecimal("amount");
           int idBuyer = rs.getInt("id_buyer");
           return new BuyOrder(idSellOrder, amount, idBuyer);
        } );
    }

    @Override
    public int getAmountPagesBySellOrderId(int id_sellorder) {
        Optional<Integer> res = jdbcTemplate.query("SELECT (count(*)-1)/"+PAGE_SIZE+"+1 AS count FROM buyorders WHERE id_sellorder = ?", new Object[]{id_sellorder}, (rs , rowNum) -> rs.getInt("count")).stream().findFirst();
        return res.orElse(0);
    }

    @Override
    public void deleteBuyOrder(int sellOrderId, int buyerId) {
        jdbcTemplate.update("DELETE FROM buyorders WHERE id_sellorder = ? AND id_buyer = ?", sellOrderId, buyerId);
    }
}
