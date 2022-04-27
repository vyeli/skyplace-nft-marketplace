package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class SellOrderJdbcDao implements SellOrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSellOrder;
    private final SimpleJdbcInsert jdbcInsertNft;
    private final SimpleJdbcInsert jdbcInsertImage;

    private static final RowMapper<SellOrder> ROW_MAPPER = (rs, rowNum) ->
        new SellOrder(rs.getLong("id"), rs.getString("seller_email"), rs.getString("descr"), rs.getBigDecimal("price"), rs.getInt("id_nft"), rs.getString("nft_addr"), rs.getString("category"));

    @Autowired
    public SellOrderJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertSellOrder = new SimpleJdbcInsert(ds)
                .withTableName("SellOrders")
                .usingGeneratedKeyColumns("id");
        jdbcInsertNft = new SimpleJdbcInsert(ds)
                .withTableName("Nfts");
        jdbcInsertImage = new SimpleJdbcInsert(ds)
                .withTableName("images")
                .usingGeneratedKeyColumns("id_image");
    }

    private List<NftCard> executeSelectNFTQuery(String query, Object[] args) {
        return jdbcTemplate.query(query, args, (rs, i) -> {
            String name = rs.getString("nft_name");
            String contract_addr = rs.getString("contract_addr");
            String chain = rs.getString("chain");
            long id_product = rs.getLong("id_product");
            long id_image = rs.getLong("id_image");
            float price = rs.getFloat("price");
            int score = 0;
            long id_nft = rs.getLong("id_nft");
            String seller_email = rs.getString("seller_email");
            String descr = rs.getString("descr");
            return new NftCard(id_image, name, chain, price, score, seller_email, descr, contract_addr, id_nft, id_product);
        });
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return jdbcTemplate.query("SELECT * FROM SellOrders WHERE id = ?", new Object[]{ id }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<NftCard> getUserSellOrders(User user){
        List<Object> args = new ArrayList<>();
        StringBuilder baseQuery = new StringBuilder("SELECT sellorders.id AS id_product, category, nfts.id AS id_nft, contract_addr, nft_name, id_image, chain, price, descr, seller_email FROM nfts NATURAL JOIN chains INNER JOIN sellorders ON (id_nft = nfts.id AND nft_addr = contract_addr) WHERE seller_email LIKE ? ORDER BY nft_name");
        args.add(user.getEmail());
        return executeSelectNFTQuery(baseQuery.toString(), args.toArray());
    }

    @Override
    public List<NftCard> getUserFavorites(User user) {
        List<Object> args = new ArrayList<>();
        StringBuilder baseQuery = new StringBuilder(
                "SELECT sellorders.id AS id_product, category, fav_nfts.id AS id_nft, contract_addr, nft_name, id_image, chain, price, descr, seller_email " +
                "FROM sellorders INNER JOIN (" +
                "SELECT user_id, id, contract_addr, chain, nft_name, id_image " +
                "FROM favorited INNER JOIN nfts " +
                "ON (id = nft_id AND contract_addr = nft_contract_addr AND nft_chain = chain)" +
                ") AS fav_nfts " +
                "ON (sellorders.id = fav_nfts.id AND nft_addr = contract_addr) " +
                "WHERE user_id = ? " +
                "ORDER BY nft_name");
        args.add(user.getId());
        return executeSelectNFTQuery(baseQuery.toString(), args.toArray());
    }

    @Override
    public boolean update(long id, String category, BigDecimal price, String description) {
        String updateQuery = "UPDATE sellorders SET category = ?, price = ?, descr = ? WHERE id = ?";
        // returns the number of affected rows
        return jdbcTemplate.update(updateQuery, category, price, description, id) == 1;
    }

    @Override
    public boolean delete(long id) {
        String updateQuery = "DELETE FROM sellorders WHERE id = ?";
        return jdbcTemplate.update(updateQuery, id) == 1;
    }

    @Override
    public SellOrder create(String name, int nftId, String nftContract, String chain, String category, BigDecimal price, String description, MultipartFile image, String email) {
        List<String> chains = jdbcTemplate.query("SELECT chain FROM chains", (rs, i) -> rs.getString("chain"));
        List<String> categories = jdbcTemplate.query("SELECT category FROM categories", (rs, i) -> rs.getString("category"));

        if(!chains.contains(chain) || !categories.contains(category))
            return new SellOrder(-1, email, description, price, nftId, nftContract, category);

        final Map<String, Object> nftData = new HashMap<>();
        nftData.put("id", nftId);
        nftData.put("contract_addr", nftContract);
        nftData.put("nft_name", name);
        nftData.put("chain", chain);

        byte[] bytes = {};
        try {
            bytes = image.getBytes();
        } catch(Exception e) {
            System.out.println("getbytes error");
        }

        final Map<String, Object> imageData = new HashMap<>();
        imageData.put("image", bytes);
        final long imageId = jdbcInsertImage.executeAndReturnKey(imageData).longValue();

        nftData.put("id_image", imageId);
        jdbcInsertNft.execute(nftData);

        final Map<String, Object> sellOrderData = new HashMap<>();
        sellOrderData.put("seller_email", email);
        sellOrderData.put("descr", description);
        sellOrderData.put("price", price);
        sellOrderData.put("id_nft", nftId);
        sellOrderData.put("nft_addr", nftContract);
        sellOrderData.put("category", category);
        sellOrderData.put("nft_chain", chain);

        final long sellOrderId = jdbcInsertSellOrder.executeAndReturnKey(sellOrderData).longValue();
        return new SellOrder(sellOrderId, email, description, price, nftId, nftContract, category);
    }
}
