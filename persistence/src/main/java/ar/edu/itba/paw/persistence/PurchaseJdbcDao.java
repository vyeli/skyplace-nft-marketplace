package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Purchase;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PurchaseJdbcDao implements PurchaseDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSellOrder;
    private final RowMapper<Purchase> ROW_MAPPER;

    @Autowired
    public PurchaseJdbcDao(final DataSource ds, UserDao userDao, NftDao nftDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertSellOrder = new SimpleJdbcInsert(ds)
                .withTableName("Purchases")
                .usingGeneratedKeyColumns("id");

        ROW_MAPPER = (rs, rownum) -> {
            User buyer = userDao.getUserById(rs.getInt("id_buyer")).orElse(new User());
            User seller = userDao.getUserById(rs.getInt("id_seller")).orElse(new User());
            Nft nft = nftDao.getNFTById(rs.getInt("id_nft")).orElse(new Nft());
            Timestamp date = rs.getTimestamp("buy_date");
            date.setNanos(0);
            return new Purchase(rs.getInt("id"), buyer, seller, nft, rs.getBigDecimal("price").stripTrailingZeros(), date);
        };
    }

    @Override
    public List<Purchase> getUserSales(int userId) {
        return jdbcTemplate.query(
            "SELECT * FROM purchases WHERE id_seller=? ORDER BY buy_date DESC",
                new Object[]{ userId },
                ROW_MAPPER
        );
    }

    @Override
    public List<Purchase> getUserPurchases(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM purchases WHERE id_buyer=? ORDER BY buy_date DESC",
                new Object[]{ userId },
                ROW_MAPPER
        );
    }

    @Override
    public List<Purchase> getAllTransactions(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM purchases WHERE id_buyer = ? OR id_seller = ? ORDER BY buy_date DESC",
                new Object[]{ userId, userId },
                ROW_MAPPER
        );
    }

    @Override
    public int createPurchase(int idBuyer, int idSeller, int idNft, BigDecimal price) {
        Map<String, Object> purchaseData = new HashMap<>();
        purchaseData.put("id_nft", idNft);
        purchaseData.put("id_buyer", idBuyer);
        purchaseData.put("id_seller", idSeller);
        purchaseData.put("price", price);
        purchaseData.put("buy_date", new Timestamp(Instant.now().toEpochMilli()));

        return jdbcInsertSellOrder.executeAndReturnKey(purchaseData).intValue();
    }
}
