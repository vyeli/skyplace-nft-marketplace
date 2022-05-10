package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.CreateImageException;
import ar.edu.itba.paw.exceptions.InvalidChainException;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ar.edu.itba.paw.persistence.Utils.USER_TABLE;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class NftJdbcDaoTest {

    private final static int NFT_ID = 1547;
    private final static String NFT_CONTRACT_ADDRESS = "0x29714cafe792ef8b8c649451d13c89e21a0d7f5b";
    private final static String NFT_NAME = "CryptoApe";
    private final static String CHAIN = "Ethereum";
    private final static int ID_IMAGE = 1;
    private final static MultipartFile MOCK_IMAGE = new MockMultipartFile("image", new byte[1]);
    private final static int ID_OWNER = 1;
    private final static String COLLECTION = "Crypto Apes Official";
    private final static String DESCRIPTION = "CryptoApes strive to unite the BAYC and CryptoPunk communities, with randomly generated and unique artwork. We are not affiliated with Yuga Labs or Larva Labs.";
    private final static String[] PROPERTIES = null;
    private final static String NFT_TABLE = "nfts";
    private final static String IMAGE_TABLE = "images";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertNft;
    private NftJdbcDao nftJdbcDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        nftJdbcDao = new NftJdbcDao(ds, new ImageJdbcDao(ds));
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertNft = new SimpleJdbcInsert(ds)
                .withTableName("nfts")
                .usingGeneratedKeyColumns("id");
        MockitoAnnotations.initMocks(this);
        SimpleJdbcInsert userJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(USER_TABLE);
        SimpleJdbcInsert imageJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName(IMAGE_TABLE);
        userJdbcInsert.execute(Utils.createUserData(ID_OWNER));
        userJdbcInsert.execute(Utils.createUserData(ID_OWNER+1));
        imageJdbcInsert.execute(Utils.createImageData(ID_IMAGE));
        JdbcTestUtils.deleteFromTables(jdbcTemplate, NFT_TABLE);
    }

    @Test
    public void testCreateNft() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);

        Optional<Nft> nft = nftJdbcDao.create(NFT_ID, NFT_CONTRACT_ADDRESS, NFT_NAME, CHAIN, MOCK_IMAGE, ID_OWNER, COLLECTION, DESCRIPTION, PROPERTIES);

        assertTrue(nft.isPresent());
        assertEquals(NFT_CONTRACT_ADDRESS, nft.get().getContractAddr());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testCreateNftWithoutChain() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);

        assertThrows(InvalidChainException.class, () -> nftJdbcDao.create(NFT_ID, NFT_CONTRACT_ADDRESS, NFT_NAME, "CHAIN", MOCK_IMAGE, ID_OWNER, COLLECTION, DESCRIPTION, PROPERTIES));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testCreateNftWithInvalidImage() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);

        assertThrows(CreateImageException.class, () -> nftJdbcDao.create(NFT_ID, NFT_CONTRACT_ADDRESS, NFT_NAME, CHAIN, null, ID_OWNER, COLLECTION, DESCRIPTION, PROPERTIES));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
        assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testGetNftById() {
        int nftId = jdbcInsertNft.executeAndReturnKey(Utils.createNftData(NFT_ID, ID_IMAGE, ID_OWNER)).intValue();

        Optional<Nft> nft = nftJdbcDao.getNFTById(nftId);

        assertTrue(nft.isPresent());
        assertEquals(NFT_ID, nft.get().getNftId());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testGetNftNotExists() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, IMAGE_TABLE);

        Optional<Nft> nft = nftJdbcDao.getNFTById(5);

        assertFalse(nft.isPresent());
    }

    @Test
    public void testGetAllPublications() {
        for(int i = 0; i < 5; i++)
            jdbcInsertNft.execute(Utils.createNftData(i, ID_IMAGE, ID_OWNER));

        List<Publication> publications = nftJdbcDao.getAllPublications(1, 12, "notSale", null, "Ethereum", null, null, "priceAsc", null, null, null);

        assertEquals(5, publications.size());
    }

    @Test
    public void testGetAllPublicationsPages() {
        int pageSize = 12;
        for(int i = 0; i < pageSize*3; i++)
            jdbcInsertNft.execute(Utils.createNftData(i, ID_IMAGE, ID_OWNER));

        List<Publication> publications = nftJdbcDao.getAllPublications(1, pageSize, "onSale,notSale", null, "Ethereum", null, null, "priceDsc", null, null, null);

        assertEquals(pageSize, publications.size());
    }

    @Test
    public void testGetAllOnSalePublications() {
        for(int i = 0; i < 5; i++)
            jdbcInsertNft.execute(Utils.createNftData(i, ID_IMAGE, ID_OWNER));

        List<Publication> publications = nftJdbcDao.getAllPublications(1, 12, "onSale", "Other,Art", "Ethereum", new BigDecimal(1), new BigDecimal(2), "name", "abc", new User(0, ""), null);

        assertEquals(0, publications.size());
    }

    @Test
    public void testGetAllPublicationsByUser() {
        for(int i = 0; i < 5; i++)
            jdbcInsertNft.execute(Utils.createNftData(i, ID_IMAGE, ID_OWNER));
        User user = new User(ID_OWNER, "");

        List<Publication> publications = nftJdbcDao.getAllPublicationsByUser(1, 12, user, null, false, false, "noSort");

        assertEquals(5, publications.size());
    }

    @Test
    public void testGetAmountPublications() {
        for(int i = 0; i < 30; i++)
            jdbcInsertNft.execute(Utils.createNftData(i, ID_IMAGE, ID_OWNER));

        int amountPublications = nftJdbcDao.getAmountPublications( "onSale,notSale", null, "Ethereum", null, null, null, null);

        assertEquals(30, amountPublications);
    }

    @Test
    public void testChangeNftOwner() {
        int nftId = jdbcInsertNft.executeAndReturnKey(Utils.createNftData(NFT_ID, ID_IMAGE, ID_OWNER)).intValue();

        nftJdbcDao.updateOwner(nftId, ID_OWNER+1);

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NFT_TABLE, "id_owner="+(ID_OWNER+1)));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NFT_TABLE, "id_owner="+ID_OWNER));
    }

    @Test
    public void testDeleteNft() {
        int nftId = jdbcInsertNft.executeAndReturnKey(Utils.createNftData(NFT_ID, ID_IMAGE, ID_OWNER)).intValue();
        for(int i = 0; i < 3; i++) {
            jdbcInsertNft.execute(Utils.createNftData(i, ID_IMAGE, ID_OWNER));
        }

        nftJdbcDao.delete(nftId);

        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Mock
    private ResultSet rs;

    @Test
    public void testCreateNftFromResultSet() throws SQLException {
        Mockito.when(rs.getInt("id")).thenReturn(1);
        Mockito.when(rs.getInt("nftId")).thenReturn(NFT_ID);
        Mockito.when(rs.getString("contractAddr")).thenReturn(NFT_CONTRACT_ADDRESS);
        Mockito.when(rs.getString("nftName")).thenReturn(NFT_NAME);
        Mockito.when(rs.getString("chain")).thenReturn(CHAIN);
        Mockito.when(rs.getInt("idImage")).thenReturn(ID_IMAGE);
        Mockito.when(rs.getInt("idOwner")).thenReturn(ID_OWNER);
        Mockito.when(rs.getString("collection")).thenReturn(NFT_CONTRACT_ADDRESS);
        Mockito.when(rs.getString("description")).thenReturn(DESCRIPTION);
        Mockito.when(rs.getArray("properties")).thenReturn(null);
        Mockito.when(rs.getInt("sellOrderId")).thenReturn(0);

        Nft nft = nftJdbcDao.createNftFromResultSet(rs);

        assertEquals(nft.getIdOwner(), ID_OWNER);
        assertEquals(nft.getContractAddr(), NFT_CONTRACT_ADDRESS);
        assertEquals(nft.getNftName(), NFT_NAME);
    }

}
