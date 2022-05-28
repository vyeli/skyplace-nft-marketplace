package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CreateUserException;
import ar.edu.itba.paw.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.User;
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
import java.util.*;

import static ar.edu.itba.paw.persistence.Utils.*;
import static org.junit.Assert.*;

/*
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJdbcDaoTest {

    private final String USER_EMAIL = "test@test.com";
    private final String USER_USERNAME = "username";
    private final String USER_PASSWORD = "password";
    private final String USER_WALLET = "0xb974dA40Aa4f54ebfbEf43BbE82A30673049d8A2";
    private final String USER_WALLET_CHAIN = "Ethereum";
    private final int IMAGE_ID = 1;
    private final int NFT_ID = 1;
    private final int SELLORDER_ID = 1;
    private final int USER_ID = 1;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert userJdbcInsert;
    private SimpleJdbcInsert nftJdbcInsert;
    private SimpleJdbcInsert imageJdbcInsert;
    private SimpleJdbcInsert sellOrderJdbcInsert;
    private UserJdbcDao userJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        userJdbcDao = new UserJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        userJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE)
                .usingGeneratedKeyColumns("id");
        nftJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(NFT_TABLE);
        imageJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE);
        sellOrderJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(SELLORDER_TABLE);
    }

    @Test
    public void testCreateUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USER_TABLE);

        Optional<User> user = userJdbcDao.create(USER_EMAIL, USER_USERNAME, USER_WALLET, USER_WALLET_CHAIN, USER_PASSWORD);

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
        assertEquals(USER_WALLET, user.get().getWallet());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testGetUserByEmail() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("wallet", USER_WALLET);
        userData.put("email", USER_EMAIL);
        userData.put("password", USER_PASSWORD);
        userData.put("wallet_chain", USER_WALLET_CHAIN);
        userData.put("role", "User");
        userData.put("id", USER_ID);
        userJdbcInsert.execute(userData);

        Optional<User> user = userJdbcDao.getUserByEmail(USER_EMAIL);

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
    }

    @Test
    public void testGetUserById() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("wallet", USER_WALLET);
        userData.put("email", USER_EMAIL);
        userData.put("password", USER_PASSWORD);
        userData.put("wallet_chain", USER_WALLET_CHAIN);
        userData.put("role", "User");
        int userId = userJdbcInsert.executeAndReturnKey(userData).intValue();

        Optional<User> user = userJdbcDao.getUserById(userId);

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
    }

    @Test
    public void testUserDuplicated() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", USER_USERNAME);
        userData.put("wallet", USER_WALLET);
        userData.put("email", USER_EMAIL);
        userData.put("password", USER_PASSWORD);
        userData.put("wallet_chain", USER_WALLET_CHAIN);
        userData.put("role", "User");
        userJdbcInsert.execute(userData);

        assertThrows(UserAlreadyExistsException.class, () -> userJdbcDao.create(USER_EMAIL, USER_USERNAME, USER_WALLET, USER_WALLET_CHAIN, USER_PASSWORD));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testChainInvalid() {
        assertThrows(CreateUserException.class, () -> userJdbcDao.create(USER_EMAIL, USER_USERNAME, USER_WALLET, "CHAIN", USER_PASSWORD));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, USER_TABLE));
    }

    @Test
    public void testUserOwnsNft() {
        int userId = userJdbcInsert.executeAndReturnKey(Utils.createUserData(USER_ID)).intValue();
        imageJdbcInsert.execute(Utils.createImageData(IMAGE_ID));
        nftJdbcInsert.execute(Utils.createNftData(NFT_ID, IMAGE_ID, userId));

        boolean userOwnsNft = userJdbcDao.userOwnsNft(NFT_ID, new User(userId, ""));

        assertTrue(userOwnsNft);
    }

    @Test
    public void testUserOwnsSellOrder() {
        int userId = userJdbcInsert.executeAndReturnKey(Utils.createUserData(USER_ID)).intValue();
        imageJdbcInsert.execute(Utils.createImageData(IMAGE_ID));
        nftJdbcInsert.execute(Utils.createNftData(NFT_ID, IMAGE_ID, userId));
        sellOrderJdbcInsert.execute(Utils.createSellOrderData(SELLORDER_ID, NFT_ID));

        boolean userOwnsSellOrder = userJdbcDao.userOwnsSellOrder(SELLORDER_ID, new User(userId, ""));

        assertTrue(userOwnsSellOrder);
    }
}

 */
