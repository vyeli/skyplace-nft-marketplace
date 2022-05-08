package ar.edu.itba.paw.persistence;

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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJdbcDaoTest {

    private final String USER_EMAIL = "test@test.com";
    private final String USER_USERNAME = "username";
    private final String USER_PASSWORD = "password";
    private final String USER_WALLET = "0xb974dA40Aa4f54ebfbEf43BbE82A30673049d8A2";
    private final String USER_WALLET_CHAIN = "Ethereum";

    private static final String USERS_TABLE = "users";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert userJdbcInsert;
    private UserJdbcDao userJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        userJdbcDao = new UserJdbcDao(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        userJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Test
    public void testCreateUser() {
        Optional<User> user = userJdbcDao.create(USER_EMAIL, USER_USERNAME, USER_WALLET, USER_WALLET_CHAIN, USER_PASSWORD);

        assertTrue(user.isPresent());
        assertEquals(USER_EMAIL, user.get().getEmail());
        assertEquals(USER_WALLET, user.get().getWallet());
        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
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

        Optional<User> user = userJdbcDao.create(USER_EMAIL, USER_USERNAME, USER_WALLET, USER_WALLET_CHAIN, USER_PASSWORD);

        assertFalse(user.isPresent());
        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }

    @Test
    public void testChainInvalid() {
        Optional<User> user = userJdbcDao.create(USER_EMAIL, USER_USERNAME, USER_WALLET, "CHAIN", USER_PASSWORD);

        assertFalse(user.isPresent());
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
    }
}
