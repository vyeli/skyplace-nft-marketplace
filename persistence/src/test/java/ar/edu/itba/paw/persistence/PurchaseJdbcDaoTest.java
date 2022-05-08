package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Purchase;
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
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PurchaseJdbcDaoTest {

    private static final String PURCHASE_TABLE = "purchases";
    private static final BigDecimal PRICE = new BigDecimal(5);
    private final int USER_ID = 1;
    private final int USER2_ID = 2;
    private final int ID_NFT = 1;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert purchaseJdbcInsert;
    private PurchaseJdbcDao purchaseJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        purchaseJdbcDao = new PurchaseJdbcDao(ds, new UserJdbcDao(ds), new NftJdbcDao(ds, new ImageJdbcDao(ds)));
        jdbcTemplate = new JdbcTemplate(ds);
        purchaseJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(PURCHASE_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Test
    public void testGetUserSales() {
        List<Purchase> purchases = purchaseJdbcDao.getUserSales(USER_ID);

        assertNotNull(purchases);
        assertEquals(0, purchases.size());
    }

    @Test
    public void testGetUserPurchases() {
        Map<String, Object> purchasesData = new HashMap<>();
        purchasesData.put("id_nft", ID_NFT);
        purchasesData.put("id_buyer", USER_ID);
        purchasesData.put("price", PRICE);
        purchasesData.put("id_seller", USER2_ID);
        purchasesData.put("buy_date", new Timestamp(LocalTime.now().getNano()));
        purchaseJdbcInsert.execute(purchasesData);
        purchasesData.put("id_buyer", USER2_ID);
        purchasesData.put("id_seller", USER_ID);
        purchaseJdbcInsert.execute(purchasesData);

        List<Purchase> purchases = purchaseJdbcDao.getUserPurchases(USER_ID);

        assertNotNull(purchases);
        assertEquals(1, purchases.size());
        assertEquals(ID_NFT, purchases.get(0).getNft().getNftId());
    }

    @Test
    public void testGetAllTransactions() {
        Map<String, Object> purchasesData = new HashMap<>();
        purchasesData.put("id_nft", ID_NFT);
        purchasesData.put("id_buyer", USER_ID);
        purchasesData.put("price", PRICE);
        purchasesData.put("id_seller", USER2_ID);
        purchasesData.put("buy_date", new Timestamp(LocalTime.now().getNano()));
        purchaseJdbcInsert.execute(purchasesData);
        purchasesData.put("id_buyer", USER2_ID);
        purchasesData.put("id_seller", USER_ID);
        purchaseJdbcInsert.execute(purchasesData);

        List<Purchase> purchases = purchaseJdbcDao.getAllTransactions(USER_ID);

        assertNotNull(purchases);
        assertEquals(2, purchases.size());
        assertEquals(ID_NFT, purchases.get(0).getNft().getNftId());
    }

    @Test
    public void testCreatePurchase() {
        int purchaseId = purchaseJdbcDao.createPurchase(USER_ID, USER2_ID, ID_NFT, PRICE);

        assertTrue(purchaseId > 0);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PURCHASE_TABLE));
    }
}
