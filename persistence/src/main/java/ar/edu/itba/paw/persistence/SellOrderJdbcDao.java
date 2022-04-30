package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.SellOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

@Repository
public class SellOrderJdbcDao implements SellOrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSellOrder;
    private final CategoryDao categoryDao;
    private final NftDao nftDao;

    private static final RowMapper<SellOrder> ROW_MAPPER = (rs, rowNum) ->
            new SellOrder(rs.getLong("id"), rs.getBigDecimal("price"), rs.getLong("id_nft"), rs.getString("category"));

    @Autowired
    public SellOrderJdbcDao(final DataSource ds, final CategoryDao categoryDao, final NftDao nftDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertSellOrder = new SimpleJdbcInsert(ds)
                .withTableName("SellOrders")
                .usingGeneratedKeyColumns("id");
        this.categoryDao = categoryDao;
        this.nftDao = nftDao;
    }

    @Override
    public Optional<SellOrder> create(BigDecimal price, String id_nft, String category) {
        List<String> categories = categoryDao.getCategories();
        if (!categories.contains(category))
            return Optional.empty();

        Optional<Nft> nft = nftDao.getNFTById(id_nft);
        if (!nft.isPresent())
            return Optional.empty();
        long id_nft_long;
        try {
            id_nft_long = Long.parseLong(id_nft);
        } catch (Exception e) {
            return Optional.empty();
        }

        Map<String, Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", price);
        sellOrderData.put("id_nft", id_nft_long);
        sellOrderData.put("category", category);

        long id = jdbcInsertSellOrder.executeAndReturnKey(sellOrderData).longValue();

        return Optional.of(new SellOrder(id, price, id_nft_long, category));
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return jdbcTemplate.query("SELECT * FROM SellOrders WHERE id = ?", new Object[]{id}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public boolean update(long id, String category, BigDecimal price) {
        String updateQuery = "UPDATE sellorders SET category = ?, price = ? WHERE id = ?";
            // returns the number of affected rows
        return jdbcTemplate.update(updateQuery, category, price, id) == 1;
    }

    @Override
    public boolean delete(long id) {
        String updateQuery = "DELETE FROM sellorders WHERE id = ?";
        return jdbcTemplate.update(updateQuery, id) == 1;
    }

    @Override
    public long getNftWithOrder(String id) {
        try {
            long idToLong = Long.parseLong(id);
            List<Long> nftId = jdbcTemplate.query("SELECT id_nft FROM sellorders WHERE id = ?", new Object[]{idToLong}, (rs , rn) -> rs.getLong("id_nft"));
            if(nftId.size() > 0)
                return nftId.get(0);
            return -1;
        } catch (Exception e) {
            return -1;
        }
    }
}
