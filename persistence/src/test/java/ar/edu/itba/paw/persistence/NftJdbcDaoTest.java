package ar.edu.itba.paw.persistence;

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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class NftJdbcDaoTest {

    // Information from https://etherscan.io/address/0x29714cafe792ef8b8c649451d13c89e21a0d7f5b
    private final int NFT_ID = 1547;
    private final String NFT_CONTRACT_ADDRESS = "0x29714cafe792ef8b8c649451d13c89e21a0d7f5b";
    private final String NFT_NAME = "CryptoApe";
    private final String CHAIN = "Ethereum";
    private final int ID_IMAGE = 1;
    private final MultipartFile MOCK_IMAGE = new MockMultipartFile("image", new byte[1]);
    private final int ID_OWNER = 1;
    private final String COLLECTION = "Crypto Apes Official";
    private final String DESCRIPTION = "CryptoApes strive to unite the BAYC and CryptoPunk communities, with randomly generated and unique artwork. We are not affiliated with Yuga Labs or Larva Labs.";
    private final String[] PROPERTIES = null;
    private final String NFT_TABLE = "nfts";
    private final String IMAGE_TABLE = "images";

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
    }

    @Test
    public void testCreateNft() {
        Optional<Nft> nft = nftJdbcDao.create(NFT_ID, NFT_CONTRACT_ADDRESS, NFT_NAME, CHAIN, MOCK_IMAGE, ID_OWNER, COLLECTION, DESCRIPTION, PROPERTIES);

        assertTrue(nft.isPresent());
        assertEquals(NFT_CONTRACT_ADDRESS, nft.get().getContractAddr());
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testCreateNftWithoutChain() {
        Optional<Nft> nft = nftJdbcDao.create(NFT_ID, NFT_CONTRACT_ADDRESS, NFT_NAME, "CHAIN", MOCK_IMAGE, ID_OWNER, COLLECTION, DESCRIPTION, PROPERTIES);

        assertFalse(nft.isPresent());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testCreateNftWithInvalidImage() {
        Optional<Nft> nft = nftJdbcDao.create(NFT_ID, NFT_CONTRACT_ADDRESS, NFT_NAME, CHAIN, null, ID_OWNER, COLLECTION, DESCRIPTION, PROPERTIES);

        assertFalse(nft.isPresent());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, IMAGE_TABLE));
        assertEquals(2, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testGetNftById() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("nft_id", NFT_ID);
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        int nftId = jdbcInsertNft.executeAndReturnKey(nftData).intValue();

        Optional<Nft> nft = nftJdbcDao.getNFTById(nftId);

        assertTrue(nft.isPresent());
        assertEquals(NFT_ID, nft.get().getNftId());
        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
    }

    @Test
    public void testGetNftNotExists() {
        Optional<Nft> nft = nftJdbcDao.getNFTById(5);

        assertFalse(nft.isPresent());
    }

    @Test
    public void testGetAllPublications() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        for(int i = 1; i < 5; i++) {
            nftData.put("nft_id", i);
            jdbcInsertNft.execute(nftData);
        }

        List<Publication> publications = nftJdbcDao.getAllPublications(1, 12, "notSale", null, "Ethereum", null, null, "priceAsc", null, null);

        assertEquals(5, publications.size());
    }

    @Test
    public void testGetAllPublicationsPages() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        for(int i = 1; i < 30; i++) {
            nftData.put("nft_id", i);
            jdbcInsertNft.execute(nftData);
        }

        List<Publication> publications = nftJdbcDao.getAllPublications(1, 12, "onSale,notSale", null, "Ethereum", null, null, "priceDsc", null, null);

        assertEquals(12, publications.size());
    }

    @Test
    public void testGetAllOnSalePublications() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        for(int i = 1; i < 5; i++) {
            nftData.put("nft_id", i);
            jdbcInsertNft.execute(nftData);
        }

        List<Publication> publications = nftJdbcDao.getAllPublications(1, 12, "onSale", "Other,Art", "Ethereum", new BigDecimal(1), new BigDecimal(2), "name", "abc", new User(0, ""));

        assertEquals(0, publications.size());
    }

    @Test
    public void testGetAllPublicationsByUser() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        for(int i = 1; i < 5; i++) {
            nftData.put("nft_id", i);
            jdbcInsertNft.execute(nftData);
        }
        User user = new User(ID_OWNER, "");

        List<Publication> publications = nftJdbcDao.getAllPublicationsByUser(1, 12, user, null, false, false, "noSort");

        assertEquals(5, publications.size());
    }

    @Test
    public void testGetAmountPublications() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        for(int i = 1; i < 30; i++) {
            nftData.put("nft_id", i);
            jdbcInsertNft.execute(nftData);
        }

        int amountPublications = nftJdbcDao.getAmountPublications( "onSale,notSale", null, "Ethereum", null, null, null);

        assertEquals(31, amountPublications);
    }

    @Test
    public void testChangeNftOwner() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("nft_id", NFT_ID);
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        int nftId = jdbcInsertNft.executeAndReturnKey(nftData).intValue();

        nftJdbcDao.updateOwner(nftId, ID_OWNER+1);

        JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NFT_TABLE, "id_owner = "+ID_OWNER+1);

        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NFT_TABLE, "id_owner="+(ID_OWNER+1)));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, NFT_TABLE, "id_owner="+ID_OWNER));
    }

    @Test
    public void testDeleteNft() {
        Map<String, Object> nftData = new HashMap<>();
        nftData.put("nft_id", NFT_ID);
        nftData.put("contract_addr", NFT_CONTRACT_ADDRESS);
        nftData.put("nft_name", NFT_NAME);
        nftData.put("chain", CHAIN);
        nftData.put("id_image", ID_IMAGE);
        nftData.put("id_owner", ID_OWNER);
        nftData.put("collection", COLLECTION);
        nftData.put("description", DESCRIPTION);
        nftData.put("properties", PROPERTIES);
        int nftId = jdbcInsertNft.executeAndReturnKey(nftData).intValue();
        for(int i = 0; i < 3; i++) {
            nftData.put("nft_id", NFT_ID+1+i);
            jdbcInsertNft.execute(nftData);
        }

        nftJdbcDao.delete(nftId);


        assertEquals(5, JdbcTestUtils.countRowsInTable(jdbcTemplate, NFT_TABLE));
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

    @Test
    public void testDaverauLevenshtein() {
        int distance = nftJdbcDao.calculateDistance("testy","testing");

        assertEquals(3, distance);
    }
}
