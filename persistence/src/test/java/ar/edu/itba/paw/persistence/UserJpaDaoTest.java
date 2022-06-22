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
import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJpaDaoTest {

    private final String USER_EMAIL = "test@test.com";
    private final String USER_USERNAME = "username";
    private final String USER_PASSWORD = "password";
    private final String USER_WALLET = "0xb974dA40Aa4f54ebfbEf43BbE82A30673049d8A2";
    private final Chain USER_WALLET_CHAIN = Chain.Ethereum;
    private final String USER_LOCALE_TEXT = "es";
    private final Role USER_ROLE = Role.User;

    private final int IMAGE_ID = 1;

    public static final String USER_TABLE = "users";

    private final static int ID_NFT = 1;
    private final static String NFT_CONTRACT_ADDR = "0xC37575F40b525FCA00554cB627CfFF8693d6b8B4";
    private final static String NFT_NAME = "Test nft";
    private final static Chain NFT_CHAIN = Chain.Cardano;
    private final static int ID_IMAGE_NFT = 1;
    private final static String NFT_COLLECTION = "Test nfts";
    private final static String NFT_DESCRIPTION = "A test nft";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserJpaDao userJpaDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        User user = userJpaDao.create(USER_EMAIL, USER_USERNAME, USER_WALLET, USER_WALLET_CHAIN, USER_PASSWORD, USER_LOCALE_TEXT);

        em.flush();

        assertNotNull(user);
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(USER_WALLET, user.getWallet());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testGetUserByEmail() {
        User userToInsert = new User(USER_USERNAME, USER_WALLET, USER_EMAIL, USER_PASSWORD, USER_WALLET_CHAIN, USER_ROLE, USER_LOCALE_TEXT);
        em.persist(userToInsert);

        Optional<User> user = userJpaDao.getUserByEmail(USER_EMAIL);

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
    }

    @Test
    public void testGetUserById() {
        User userToInsert = new User(USER_USERNAME, USER_WALLET, USER_EMAIL, USER_PASSWORD, USER_WALLET_CHAIN, USER_ROLE, USER_LOCALE_TEXT);
        em.persist(userToInsert);

        Optional<User> user = userJpaDao.getUserById(userToInsert.getId());

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
    }

    @Test
    public void testUserOwnsNft() {
        User userToInsert = new User(USER_USERNAME, USER_WALLET, USER_EMAIL, USER_PASSWORD, USER_WALLET_CHAIN, USER_ROLE, USER_LOCALE_TEXT);
        em.persist(userToInsert);
        Image imageToInsert = new Image(new byte[]{10,20,30});
        em.persist(imageToInsert);
        Nft nftToInsert = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, imageToInsert.getIdImage(), NFT_COLLECTION, NFT_DESCRIPTION, userToInsert);
        em.persist(nftToInsert);

        boolean userOwnsNft = userJpaDao.userOwnsNft(nftToInsert.getId(), userToInsert);

        assertTrue(userOwnsNft);
    }

}
