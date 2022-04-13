package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.SellOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SellOrderJdbcDao implements SellOrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSellOrder;
    private final SimpleJdbcInsert jdbcInsertNft;

    @Autowired
    public SellOrderJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertSellOrder = new SimpleJdbcInsert(ds)
                .withTableName("SellOrders")
                .usingGeneratedKeyColumns("id");
        jdbcInsertNft = new SimpleJdbcInsert(ds)
                .withTableName("Nfts");
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return Optional.empty();
    }

    @Override
    public SellOrder create(String name, int nftId, String nftContract, String chain, String category, double price, String description, MultipartFile image, String email) {
        final Map<String, Object> nftData = new HashMap<>();
        nftData.put("id", nftId);
        nftData.put("contract_addr", nftContract);
        nftData.put("nft_name", name);
        nftData.put("chain", chain);
        nftData.put("category", category);
        String base64Encoded = "";
        try {
            byte[] bytes = image.getBytes();
            byte[] encodedBase64 = Base64.getEncoder().encode(bytes);
            base64Encoded = new String(encodedBase64, StandardCharsets.UTF_8);
        } catch(Exception e) {
            System.out.println("getbytes error");
        }

        nftData.put("img", base64Encoded);

        jdbcInsertNft.execute(nftData);

        final Map<String, Object> sellOrderData = new HashMap<>();
        sellOrderData.put("seller_email", email);
        sellOrderData.put("descr", description);
        sellOrderData.put("price", price);
        sellOrderData.put("id_nft", nftId);
        sellOrderData.put("nft_addr", nftContract);

        final long sellOrderId = jdbcInsertSellOrder.executeAndReturnKey(sellOrderData).longValue();
        return new SellOrder(sellOrderId, email, description, price, 1, "0xabcdefghijklmnopqrstuvwxyz");
    }
}
