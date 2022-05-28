/*package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.InvalidCategoryException;
import ar.edu.itba.paw.exceptions.NftNotFoundException;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ar.edu.itba.paw.persistence.Utils.*;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class SellOrderJdbcDaoTest {

    private final BigDecimal PRICE = new BigDecimal(5);
    private final int NFT_ID = 1;
    private final int USER_ID = 1;
    private final int IMAGE_ID = 1;
    private final int NFT2_ID = 2;
    private final String CATEGORY = "Other";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert sellOrderJdbcInsert;
    private SellOrderJdbcDao sellOrderJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        sellOrderJdbcDao = new SellOrderJdbcDao(ds, new CategoryJdbcDao(ds), new NftJdbcDao(ds, new ImageJdbcDao(ds)));
        jdbcTemplate = new JdbcTemplate(ds);
        sellOrderJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(SELLORDER_TABLE)
                .usingGeneratedKeyColumns("id");
        SimpleJdbcInsert nftJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(NFT_TABLE);
        SimpleJdbcInsert userJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE);
        SimpleJdbcInsert imageJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE);

        imageJdbcInsert.execute(Utils.createImageData(IMAGE_ID));
        userJdbcInsert.execute(Utils.createUserData(USER_ID));
        nftJdbcInsert.execute(Utils.createNftData(NFT_ID, IMAGE_ID, USER_ID));
        nftJdbcInsert.execute(Utils.createNftData(NFT2_ID, IMAGE_ID, USER_ID));
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SELLORDER_TABLE);
    }

    @Test
    public void testCreateSellOrder(){
        Optional<SellOrder> sellOrder = sellOrderJdbcDao.create(PRICE, NFT_ID, CATEGORY);

        assertTrue(sellOrder.isPresent());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
        assertEquals(PRICE, sellOrder.get().getPrice());
    }

    @Test
    public void testCreateSellOrderInvalidCategory() {
        assertThrows(InvalidCategoryException.class, () -> sellOrderJdbcDao.create(PRICE, NFT_ID, "category"));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

    @Test
    public void testCreateSellOrderInvalidNft() {
        assertThrows(NftNotFoundException.class, () -> sellOrderJdbcDao.create(PRICE, 15, CATEGORY));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

    @Test
    public void testGetSellOrderById() {
        Map<String,Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", PRICE);
        sellOrderData.put("id_nft", NFT2_ID);
        sellOrderData.put("category", CATEGORY);
        int sellOrderId = sellOrderJdbcInsert.executeAndReturnKey(sellOrderData).intValue();

        Optional<SellOrder> sellOrder = sellOrderJdbcDao.getOrderById(sellOrderId);

        assertTrue(sellOrder.isPresent());
        assertEquals(PRICE, sellOrder.get().getPrice());
    }

    @Test
    public void testUpdateSellOrder() {
        Map<String,Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", PRICE);
        sellOrderData.put("id_nft", NFT2_ID);
        sellOrderData.put("category", CATEGORY);
        int sellOrderId = sellOrderJdbcInsert.executeAndReturnKey(sellOrderData).intValue();

        boolean sellOrder = sellOrderJdbcDao.update(sellOrderId, "Art", new BigDecimal(15));

        assertTrue(sellOrder);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SELLORDER_TABLE, "category='Art' AND price=15"));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

    @Test
    public void testDelete() {
        Map<String,Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", PRICE);
        sellOrderData.put("id_nft", NFT2_ID);
        sellOrderData.put("category", CATEGORY);
        int sellOrderId = sellOrderJdbcInsert.executeAndReturnKey(sellOrderData).intValue();

        boolean sellOrder = sellOrderJdbcDao.delete(sellOrderId);

        assertTrue(sellOrder);
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

    @Test
    public void testDeleteNotExists() {
        Map<String,Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", PRICE);
        sellOrderData.put("id_nft", NFT2_ID);
        sellOrderData.put("category", CATEGORY);
        int sellOrderId = sellOrderJdbcInsert.executeAndReturnKey(sellOrderData).intValue();

        boolean sellOrder = sellOrderJdbcDao.delete(sellOrderId+1);

        assertFalse(sellOrder);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

    @Test
    public void testGetNftWithOrder() {
        Map<String,Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", PRICE);
        sellOrderData.put("id_nft", NFT2_ID);
        sellOrderData.put("category", CATEGORY);
        int sellOrderId = sellOrderJdbcInsert.executeAndReturnKey(sellOrderData).intValue();

        int nftId = sellOrderJdbcDao.getNftWithOrder(sellOrderId);

        assertEquals(NFT2_ID, nftId);
    }

    @Test
    public void testGetNftWithOrderNotExists() {
        Map<String,Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", PRICE);
        sellOrderData.put("id_nft", NFT2_ID);
        sellOrderData.put("category", CATEGORY);
        int sellOrderId = sellOrderJdbcInsert.executeAndReturnKey(sellOrderData).intValue();

        int nftId = sellOrderJdbcDao.getNftWithOrder(sellOrderId+1);

        assertEquals(-1, nftId);
    }
}
*/