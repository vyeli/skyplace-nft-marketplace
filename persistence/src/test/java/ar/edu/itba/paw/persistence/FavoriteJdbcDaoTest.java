package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.NftNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class FavoriteJdbcDaoTest {

    private static final String FAVORITE_TABLE = "favorited";
    private final int NFT_ID = 1;
    private final int USER_ID = 1;
    private final int USER2_ID = 2;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert favoriteJdbcInsert;
    private FavoriteJdbcDao favoriteJdbcDao;


    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        favoriteJdbcDao = new FavoriteJdbcDao(ds, new NftJdbcDao(ds, new ImageJdbcDao(ds)));
        jdbcTemplate = new JdbcTemplate(ds);
        favoriteJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(FAVORITE_TABLE);
    }

    @Test
    public void testAddNftFavorite() {
        favoriteJdbcDao.addNftFavorite(NFT_ID, new User(USER_ID, ""));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testAddNftFavoriteInvalidNft() {
        assertThrows(NftNotFoundException.class, () -> favoriteJdbcDao.addNftFavorite(30, new User(USER_ID, "")));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testAddNftFavoriteInvalidUser() {
        assertThrows(DataIntegrityViolationException.class, () -> favoriteJdbcDao.addNftFavorite(NFT_ID, new User(40, "")));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testAddNftFavoriteTwice() {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", USER_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);

        assertThrows(DuplicateKeyException.class,() -> favoriteJdbcDao.addNftFavorite(NFT_ID, new User(USER_ID, "")));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testRemoveNftFavorite() {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", USER_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);

        favoriteJdbcDao.removeNftFavorite(NFT_ID, new User(USER_ID, ""));

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testRemoveNftFavoriteInvalidNft() {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", USER_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);

        assertThrows(NftNotFoundException.class, () -> favoriteJdbcDao.removeNftFavorite(30, new User(USER_ID, "")));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testRemoveNftFavoriteInvalidUser() {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", USER_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);

        favoriteJdbcDao.removeNftFavorite(NFT_ID, new User(40, ""));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testUserFavedNft() {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", USER_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);

        boolean userFavedNft = favoriteJdbcDao.userFavedNft(USER_ID, NFT_ID);

        assertTrue(userFavedNft);
    }

    @Test
    public void testUserNotFavedNft() {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", USER_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);

        boolean userFavedNft = favoriteJdbcDao.userFavedNft(15, NFT_ID);

        assertFalse(userFavedNft);
    }

    @Test
    public void testGetNftFavorites() {
        Map<String, Object> favoritedData = new HashMap<>();
        favoritedData.put("user_id", USER_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);
        favoritedData.put("user_id", USER2_ID);
        favoritedData.put("id_nft", NFT_ID);
        favoriteJdbcInsert.execute(favoritedData);


        int amountFavorites = favoriteJdbcDao.getNftFavorites(NFT_ID);

        assertEquals(2, amountFavorites);
    }
}
