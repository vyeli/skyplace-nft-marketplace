/*
package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.NftNotFoundException;
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

import static ar.edu.itba.paw.persistence.Utils.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class FavoriteJdbcDaoTest {

    private final int NFT_ID = 1;
    private final int USER_ID = 1;
    private final int USER2_ID = 2;
    private final int IMAGE_ID = 1;

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
        SimpleJdbcInsert nftJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(NFT_TABLE);
        SimpleJdbcInsert userJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE);
        SimpleJdbcInsert imageJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE);

        imageJdbcInsert.execute(Utils.createImageData(IMAGE_ID));
        userJdbcInsert.execute(Utils.createUserData(USER_ID));
        userJdbcInsert.execute(Utils.createUserData(USER2_ID));
        nftJdbcInsert.execute(Utils.createNftData(NFT_ID, IMAGE_ID, USER_ID));
        JdbcTestUtils.deleteFromTables(jdbcTemplate, FAVORITE_TABLE);
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
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER_ID, NFT_ID));

        assertThrows(DuplicateKeyException.class,() -> favoriteJdbcDao.addNftFavorite(NFT_ID, new User(USER_ID, "")));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testRemoveNftFavorite() {
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER_ID, NFT_ID));

        favoriteJdbcDao.removeNftFavorite(NFT_ID, new User(USER_ID, ""));

        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testRemoveNftFavoriteInvalidNft() {
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER_ID, NFT_ID));

        assertThrows(NftNotFoundException.class, () -> favoriteJdbcDao.removeNftFavorite(30, new User(USER_ID, "")));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testRemoveNftFavoriteInvalidUser() {
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER_ID, NFT_ID));

        favoriteJdbcDao.removeNftFavorite(NFT_ID, new User(40, ""));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, FAVORITE_TABLE));
    }

    @Test
    public void testUserFavedNft() {
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER_ID, NFT_ID));

        boolean userFavedNft = favoriteJdbcDao.userFavedNft(USER_ID, NFT_ID);

        assertTrue(userFavedNft);
    }

    @Test
    public void testUserNotFavedNft() {
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER_ID, NFT_ID));

        boolean userFavedNft = favoriteJdbcDao.userFavedNft(15, NFT_ID);

        assertFalse(userFavedNft);
    }

    @Test
    public void testGetNftFavorites() {
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER_ID, NFT_ID));
        favoriteJdbcInsert.execute(Utils.createFavoritedData(USER2_ID, NFT_ID));


        int amountFavorites = favoriteJdbcDao.getNftFavorites(NFT_ID);

        assertEquals(2, amountFavorites);
    }
}
*/