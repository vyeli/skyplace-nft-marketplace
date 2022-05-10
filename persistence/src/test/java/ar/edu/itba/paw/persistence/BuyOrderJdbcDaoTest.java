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

import static ar.edu.itba.paw.persistence.Utils.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class BuyOrderJdbcDaoTest {
    private JdbcTemplate jdbcTemplate;
    private BuyOrderJdbcDao buyOrderJdbcDao;

    private static final int SELLORDER_ID = 1;
    private static final int NFT_ID = 1;
    private static final int IMAGE_ID = 1;
    private static final int OWNER_ID = 1;
    private static final int BUYER_ID = 2;
    private static final BigDecimal PRICE = new BigDecimal(10);

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        buyOrderJdbcDao = new BuyOrderJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        SimpleJdbcInsert buyOrderJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(BUYORDER_TABLE);
        SimpleJdbcInsert sellOrderJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(SELLORDER_TABLE);
        SimpleJdbcInsert nftJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(NFT_TABLE);
        SimpleJdbcInsert userJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE);
        SimpleJdbcInsert imageJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE);

        imageJdbcInsert.execute(Utils.createImageData(IMAGE_ID));
        userJdbcInsert.execute(Utils.createUserData(OWNER_ID));
        userJdbcInsert.execute(Utils.createUserData(BUYER_ID));
        nftJdbcInsert.execute(Utils.createNftData(NFT_ID, IMAGE_ID, OWNER_ID));
        sellOrderJdbcInsert.execute(Utils.createSellOrderData(SELLORDER_ID, NFT_ID));

        Map<String, Object> buyOrderData = new HashMap<>();
        buyOrderData.put("id_sellorder", SELLORDER_ID);
        buyOrderData.put("amount", new BigDecimal(5));
        buyOrderData.put("id_buyer", BUYER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);
        buyOrderData.put("amount", new BigDecimal(7));
        buyOrderData.put("id_buyer", OWNER_ID);
        buyOrderJdbcInsert.execute(buyOrderData);
    }

    @Test
    public void testCreateNewBuyOrder() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, BUYORDER_TABLE);

        buyOrderJdbcDao.create(SELLORDER_ID, PRICE, BUYER_ID);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate, BUYORDER_TABLE));
    }

    @Test
    public void testUpdateBuyOrder() {
        buyOrderJdbcDao.create(SELLORDER_ID, PRICE, BUYER_ID);

        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, BUYORDER_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BUYORDER_TABLE, "amount = 10"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BUYORDER_TABLE, "amount = 5"));
    }


    @Test
    public void testGetOrdersBySellOrderId() {
        List<BuyOrder> buyOrders = buyOrderJdbcDao.getOrdersBySellOrderId(1, SELLORDER_ID);

        assertEquals(2, buyOrders.size());
        assertEquals(SELLORDER_ID, buyOrders.get(0).getIdSellOrder());
    }

    @Test
    public void testGetAmountPagesBySellOrderId() {
        int amountPages = buyOrderJdbcDao.getAmountPagesBySellOrderId(SELLORDER_ID);

        assertEquals(1, amountPages);
    }

    @Test
    public void testDeleteBuyOrder() {
        buyOrderJdbcDao.deleteBuyOrder(SELLORDER_ID, BUYER_ID);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, BUYORDER_TABLE));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, BUYORDER_TABLE, "id_buyer="+BUYER_ID));
    }

}
