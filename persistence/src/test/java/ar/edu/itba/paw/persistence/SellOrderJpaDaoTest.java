package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class SellOrderJpaDaoTest {

    private final BigDecimal PRICE = BigDecimal.TEN;
    private final int NFT_ID = 1;
    private final int USER_ID = 1;
    private final int IMAGE_ID = 1;
    private final int NFT2_ID = 2;
    private final String CATEGORY = "Other";

    private final static String MAIL_USER1 = "test1@test.test";
    private final static String USERNAME_USER1 = "test1";
    private final static String WALLET_USER1 = "0x3038BfE9acde2Fa667bEbF3322DBf9F33ca22c80";
    private final static Chain WALLETCHAIN_USER1 = Chain.Ethereum;
    private final static String PASSWORD_USER1 = "PASSUSER1";
    private final static Role ROLE_USER1 = Role.User;
    private final static String LOCALE_USER1 = "en";

    private final static int ID_NFT = 1;
    private final static String NFT_CONTRACT_ADDR = "0xC37575F40b525FCA00554cB627CfFF8693d6b8B4";
    private final static String NFT_NAME = "Test nft";
    private final static Chain NFT_CHAIN = Chain.Cardano;
    private final static int ID_IMAGE_NFT = 1;

    private final static String NFT_COLLECTION = "Test nfts";
    private final static String NFT_DESCRIPTION = "A test nft";

    private final static Category NFT_CATEGORY = Category.Memes;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert sellOrderJdbcInsert;

    private final static String SELLORDER_TABLE = "sellorders";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SellOrderJpaDao sellOrderJpaDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, SELLORDER_TABLE);
    }

    @Test
    public void testCreateSellOrder(){
        User owner = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(NFT_ID, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, owner);

        em.persist(owner);
        em.persist(nft);

        SellOrder sellOrder = sellOrderJpaDao.create(PRICE, nft, NFT_CATEGORY);

        em.flush();

        assertNotNull(sellOrder);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
        assertEquals(PRICE.doubleValue(), sellOrder.getPrice().doubleValue(), 0.001);
    }

    @Test
    public void testGetSellOrderById() {
        User owner = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(NFT_ID, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, owner);
        SellOrder sellOrderToInsert = new SellOrder(PRICE, nft, NFT_CATEGORY);

        em.persist(owner);
        em.persist(nft);
        em.persist(sellOrderToInsert);

        Optional<SellOrder> sellOrder = sellOrderJpaDao.getOrderById(sellOrderToInsert.getId());

        assertTrue(sellOrder.isPresent());
        assertEquals(PRICE.doubleValue(), sellOrder.get().getPrice().doubleValue(), 0.001);
    }

    @Test
    public void testUpdateSellOrder() {
        User owner = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(NFT_ID, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, owner);
        SellOrder sellOrderToInsert = new SellOrder(PRICE, nft, NFT_CATEGORY);

        em.persist(owner);
        em.persist(nft);
        em.persist(sellOrderToInsert);

        boolean sellOrder = sellOrderJpaDao.update(sellOrderToInsert.getId(), Category.Art, BigDecimal.ONE);

        assertTrue(sellOrder);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, SELLORDER_TABLE, "category='Art' AND price=1"));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

    @Test
    public void testDelete() {
        User owner = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(NFT_ID, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, owner);
        SellOrder sellOrderToInsert = new SellOrder(PRICE, nft, NFT_CATEGORY);

        em.persist(owner);
        em.persist(nft);
        em.persist(sellOrderToInsert);

        boolean sellOrder = sellOrderJpaDao.delete(sellOrderToInsert.getId());

        assertTrue(sellOrder);
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

    @Test
    public void testDeleteNotExists() {
        User owner = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(NFT_ID, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, owner);
        SellOrder sellOrderToInsert = new SellOrder(PRICE, nft, NFT_CATEGORY);

        em.persist(owner);
        em.persist(nft);
        em.persist(sellOrderToInsert);

        boolean sellOrder = sellOrderJpaDao.delete(sellOrderToInsert.getId() + 1);

        assertFalse(sellOrder);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SELLORDER_TABLE));
    }

}