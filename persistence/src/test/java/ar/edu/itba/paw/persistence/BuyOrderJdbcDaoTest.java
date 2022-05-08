package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.BuyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class BuyOrderJdbcDaoTest {

    private static final String BUYORDER_TABLE = "buyorders";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert buyOrderJdbcInsert;
    private BuyOrderJdbcDao buyOrderJdbcDao;

    private final int SELLORDER_ID = 1;
    private final int OWNER_ID = 1;
    private final int BUYER_ID = 2;
    private final BigDecimal PRICE = new BigDecimal(10);

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        buyOrderJdbcDao = new BuyOrderJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        buyOrderJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(BUYORDER_TABLE);
    }

    @Test
    public void testCreateNewBuyOrder() {
        buyOrderJdbcDao.create(SELLORDER_ID, PRICE, BUYER_ID);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, BUYORDER_TABLE));
    }

    @Test
    public void testUpdateBuyOrder() {
        Map<String, Object> buyOrderData = new HashMap<>();
        buyOrderData.put("id_sellorder", SELLORDER_ID);
        buyOrderData.put("amount", new BigDecimal(5));
        buyOrderData.put("id_buyer", BUYER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);

        buyOrderJdbcDao.create(SELLORDER_ID, PRICE, BUYER_ID);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, BUYORDER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BUYORDER_TABLE, "amount = 10"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BUYORDER_TABLE, "amount = 5"));
    }


    @Test
    public void testGetOrdersBySellOrderId() {
        Map<String, Object> buyOrderData = new HashMap<>();
        buyOrderData.put("id_sellorder", SELLORDER_ID);
        buyOrderData.put("amount", new BigDecimal(5));
        buyOrderData.put("id_buyer", BUYER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);
        buyOrderData.put("amount", new BigDecimal(7));
        buyOrderData.put("id_buyer", OWNER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);

        List<BuyOrder> buyOrders = buyOrderJdbcDao.getOrdersBySellOrderId(1, SELLORDER_ID);

        assertEquals(2, buyOrders.size());
        assertEquals(SELLORDER_ID, buyOrders.get(0).getIdSellOrder());
    }

    @Test
    public void testGetAmountPagesBySellOrderId() {
        Map<String, Object> buyOrderData = new HashMap<>();
        buyOrderData.put("id_sellorder", SELLORDER_ID);
        buyOrderData.put("amount", new BigDecimal(5));
        buyOrderData.put("id_buyer", BUYER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);
        buyOrderData.put("amount", new BigDecimal(7));
        buyOrderData.put("id_buyer", OWNER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);

        int amountPages = buyOrderJdbcDao.getAmountPagesBySellOrderId(SELLORDER_ID);

        assertEquals(1, amountPages);
    }

    @Test
    public void testDeleteBuyOrder() {
        Map<String, Object> buyOrderData = new HashMap<>();
        buyOrderData.put("id_sellorder", SELLORDER_ID);
        buyOrderData.put("amount", new BigDecimal(5));
        buyOrderData.put("id_buyer", BUYER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);
        buyOrderData.put("amount", new BigDecimal(7));
        buyOrderData.put("id_buyer", OWNER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);

        buyOrderJdbcDao.deleteBuyOrder(SELLORDER_ID, BUYER_ID);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, BUYORDER_TABLE));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BUYORDER_TABLE, "id_buyer="+BUYER_ID));
    }
}
