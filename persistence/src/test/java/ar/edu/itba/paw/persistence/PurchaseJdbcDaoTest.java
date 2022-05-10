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
import java.util.List;

import static ar.edu.itba.paw.persistence.Utils.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PurchaseJdbcDaoTest {


    private static final BigDecimal PRICE = new BigDecimal(5);
    private final int USER_ID = 1;
    private final int USER2_ID = 2;
    private final int ID_NFT = 1;
    private final int IMAGE_ID = 1;

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
        SimpleJdbcInsert nftJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(NFT_TABLE);
        SimpleJdbcInsert userJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE);
        SimpleJdbcInsert imageJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE);

        imageJdbcInsert.execute(Utils.createImageData(IMAGE_ID));
        userJdbcInsert.execute(Utils.createUserData(USER_ID));
        userJdbcInsert.execute(Utils.createUserData(USER2_ID));
        nftJdbcInsert.execute(Utils.createNftData(ID_NFT, IMAGE_ID, USER_ID));
        JdbcTestUtils.deleteFromTables(jdbcTemplate, PURCHASE_TABLE);
    }

    @Test
    public void testGetUserSales() {
        List<Purchase> purchases = purchaseJdbcDao.getUserSales(USER_ID);

        assertNotNull(purchases);
        assertEquals(0, purchases.size());
    }

    @Test
    public void testGetUserPurchases() {
        purchaseJdbcInsert.execute(Utils.createPurchaseData(ID_NFT, USER_ID, USER2_ID));
        purchaseJdbcInsert.execute(Utils.createPurchaseData(ID_NFT, USER2_ID, USER_ID));

        List<Purchase> purchases = purchaseJdbcDao.getUserPurchases(USER_ID);

        assertNotNull(purchases);
        assertEquals(1, purchases.size());
        assertEquals(ID_NFT, purchases.get(0).getNft().getNftId());
    }

    @Test
    public void testGetAllTransactions() {
        purchaseJdbcInsert.execute(Utils.createPurchaseData(ID_NFT, USER_ID, USER2_ID));
        purchaseJdbcInsert.execute(Utils.createPurchaseData(ID_NFT, USER2_ID, USER_ID));

        List<Purchase> purchases = purchaseJdbcDao.getAllTransactions(USER_ID, 1, 12);

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
