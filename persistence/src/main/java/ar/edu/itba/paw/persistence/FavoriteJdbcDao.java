package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FavoriteJdbcDao implements FavoriteDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteJdbcDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertFavorited;
    private final NftDao nftDao;

    @Autowired
    public FavoriteJdbcDao(DataSource ds, NftDao nftDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertFavorited = new SimpleJdbcInsert(ds)
                .withTableName("favorited");
        this.nftDao = nftDao;
    }


    @Override
    public void addNftFavorite(int productId, User user) {
        if(!nftDao.getNFTById(productId).isPresent())
            return;

        Map<String, Object> favoriteData = new HashMap<>();
        favoriteData.put("user_id", user.getId());
        favoriteData.put("id_nft", productId);
        try {
            jdbcInsertFavorited.execute(favoriteData);
        } catch (Exception e){
            LOGGER.error("FavoriteDao: " + e.getMessage());
        }
    }

    @Override
    public void removeNftFavorite(int productId, User user) {
        if(!nftDao.getNFTById(productId).isPresent())
            return;

        try {
            jdbcTemplate.update("DELETE FROM favorited WHERE user_id=? AND id_nft=?", user.getId(), productId);
        } catch (Exception e){
            LOGGER.error("FavoriteDao: " + e.getMessage());
        }
    }

    @Override
    public boolean userFavedNft(int userId, int idNft) {
        return jdbcTemplate.query("SELECT * FROM favorited WHERE user_id=? AND id_nft=?", new Object[]{userId, idNft}, (rs, rownum) -> rs.getString("id_nft")).size() > 0;
    }

    @Override
    public int getNftFavorites(int productId) {
        return jdbcTemplate.query("SELECT count(*) AS count FROM favorited WHERE id_nft=?", new Object[]{productId}, (rs, rownum) -> rs.getInt("count")).stream().findFirst().orElse(0);
    }
}
