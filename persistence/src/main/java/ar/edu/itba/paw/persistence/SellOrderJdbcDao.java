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
            new SellOrder(rs.getInt("id"), rs.getBigDecimal("price"), rs.getInt("id_nft"), rs.getString("category"));

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
    public Optional<SellOrder> create(BigDecimal price, int idNft, String category) {
        List<String> categories = categoryDao.getCategories();
        if (!categories.contains(category))
            return Optional.empty();

        Optional<Nft> nft = nftDao.getNFTById(idNft);
        if (!nft.isPresent())
            return Optional.empty();

        Map<String, Object> sellOrderData = new HashMap<>();
        sellOrderData.put("price", price);
        sellOrderData.put("id_nft", idNft);
        sellOrderData.put("category", category);

        int id = jdbcInsertSellOrder.executeAndReturnKey(sellOrderData).intValue();

        return Optional.of(new SellOrder(id, price, idNft, category));
    }

    @Override
    public Optional<SellOrder> getOrderById(int id) {
        return jdbcTemplate.query("SELECT * FROM SellOrders WHERE id = ?", new Object[]{id}, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public boolean update(int id, String category, BigDecimal price) {
        String updateQuery = "UPDATE sellorders SET category = ?, price = ? WHERE id = ?";
            // returns the number of affected rows
        return jdbcTemplate.update(updateQuery, category, price, id) == 1;
    }

    @Override
    public boolean delete(int id) {
        String updateQuery = "DELETE FROM sellorders WHERE id = ?";
        return jdbcTemplate.update(updateQuery, id) == 1;
    }

    @Override
    public int getNftWithOrder(int id) {
        List<Integer> nftId = jdbcTemplate.query("SELECT id_nft FROM sellorders WHERE id = ?", new Object[]{id}, (rs , rn) -> rs.getInt("id_nft"));
        if(nftId.size() > 0)
            return nftId.get(0);
        return -1;
    }
}
