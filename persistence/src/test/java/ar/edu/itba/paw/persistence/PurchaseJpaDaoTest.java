package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PurchaseJpaDaoTest {

    private final static String MAIL_USER1 = "test1@test.test";
    private final static String USERNAME_USER1 = "test1";
    private final static String WALLET_USER1 = "0x3038BfE9acde2Fa667bEbF3322DBf9F33ca22c80";
    private final static Chain WALLETCHAIN_USER1 = Chain.Ethereum;
    private final static String PASSWORD_USER1 = "PASSUSER1";
    private final static Role ROLE_USER1 = Role.User;
    private final static String LOCALE_USER1 = "en";

    private final static String MAIL_USER2 = "test2@test.test";
    private final static String USERNAME_USER2 = "test2";
    private final static String WALLET_USER2 = "0xE5Da717E6d0186bE40eEa87De0A5aC9d5c4e5666";
    private final static Chain WALLETCHAIN_USER2 = Chain.Ethereum;
    private final static String PASSWORD_USER2 = "PASSUSER2";
    private final static Role ROLE_USER2 = Role.User;
    private final static String LOCALE_USER2 = "en";

    private final static int ID_NFT = 1;
    private final static String NFT_CONTRACT_ADDR = "0xC37575F40b525FCA00554cB627CfFF8693d6b8B4";
    private final static String NFT_NAME = "Test nft";
    private final static Chain NFT_CHAIN = Chain.Cardano;
    private final static int ID_IMAGE_NFT = 1;
    private final static String NFT_COLLECTION = "Test nfts";
    private final static String NFT_DESCRIPTION = "A test nft";

    private final static int PAGE_NUM = 1;

    private final static int ID_PURCHASE = 1;
    private final static StatusPurchase STATUS_PURCHASE = StatusPurchase.SUCCESS;
    private final static Date DATE_PURCHASE = null;
    private final static String TX_HASH = "3E5XGTx46hAJYUNFZ5a8EhGZ7hXR8irenxTozSdZ84gq4N1hqvSGAtuvm9aTKGTRjaFbU1TDLtdQFg4GHK5XxiSi";
    private final static String TX_BASE_HASH = "3E5XGTx46hAJYUNFZ5a8EhGZ7hXR8irenxTozSdZ84gq4N1hqvSGAtuvm9aTKGTRjaFbU1TDLtdQFg4GHK5XxiS";

    private final static BigDecimal PURCHASE_PRICE = BigDecimal.ONE;

    private final static String PURCHASE_TABLE = "purchases";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PurchaseJpaDao purchaseJpaDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, PURCHASE_TABLE);
    }

    @Test
    public void testCreatePurchase(){
        User seller = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);

        em.persist(seller);
        em.persist(bidder);
        em.persist(nft);

        Purchase purchase = purchaseJpaDao.createPurchase(bidder, seller, nft, PURCHASE_PRICE, TX_HASH, STATUS_PURCHASE);

        em.flush();

        assertNotNull(purchase);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, PURCHASE_TABLE));
        assertEquals(TX_HASH, purchase.getTxHash());
    }

    @Test
    public void testGetTransactionAmount(){
        User seller = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        em.persist(seller);

        for(int i=0 ; i < 5 ; i++){
            User bidder = new User(USERNAME_USER1 + i, WALLET_USER1, MAIL_USER1 + i, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
            em.persist(bidder);
            Nft nft = new Nft(ID_NFT + i, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
            em.persist(nft);
            Purchase purchase = new Purchase(PURCHASE_PRICE, new Date(0), nft, bidder, seller, STATUS_PURCHASE, TX_BASE_HASH + i);
            em.persist(purchase);
        }

        int transactionAmount = purchaseJpaDao.getTransactionAmount(seller.getId());

        assertEquals(5, transactionAmount);
    }

    @Test
    public void testGetAllTransactions(){
        User seller = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        em.persist(seller);

        for(int i=0 ; i < 5 ; i++){
            User bidder = new User(USERNAME_USER1 + i, WALLET_USER1, MAIL_USER1 + i, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
            em.persist(bidder);
            Nft nft = new Nft(ID_NFT + i, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
            em.persist(nft);
            Purchase purchase = new Purchase(PURCHASE_PRICE, new Date(0), nft, bidder, seller, STATUS_PURCHASE, TX_BASE_HASH + i);
            em.persist(purchase);
        }

        List<Purchase> transactions = purchaseJpaDao.getAllTransactions(seller.getId(), 1, 5);

        assertEquals(5, transactions.size());
    }

    @Test
    public void testIsTxAlreadyInUseOnTxNotUsed(){
        User seller = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);

        em.persist(seller);
        em.persist(bidder);
        em.persist(nft);

        boolean result = purchaseJpaDao.isTxHashAlreadyInUse(TX_HASH);

        assertFalse(result);
    }

    @Test
    public void testIsTxAlreadyInUseOnTxUsed(){
        User seller = new User(USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
        Purchase purchase = new Purchase(PURCHASE_PRICE, new Date(0), nft, bidder, seller, STATUS_PURCHASE, TX_HASH);

        em.persist(seller);
        em.persist(bidder);
        em.persist(nft);
        em.persist(purchase);

        boolean result = purchaseJpaDao.isTxHashAlreadyInUse(TX_HASH);

        assertTrue(result);
    }

}
