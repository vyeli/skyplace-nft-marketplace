package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ImageJdbcDao implements ImageDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertImage;

    private static final RowMapper<byte[]> ROW_MAPPER = (rs, rowNum) -> rs.getBytes("image");

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertImage = new SimpleJdbcInsert(ds)
                .withTableName("images")
                .usingGeneratedKeyColumns("id_image");
    }

    @Override
    public Optional<Integer> createImage(MultipartFile image) {
        try {
            byte[] bytes = image.getBytes();
            final Map<String, Object> imageData = new HashMap<>();
            imageData.put("image", bytes);
            return Optional.of(jdbcInsertImage.executeAndReturnKey(imageData).longValue());
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    public Image getImage(int id) {
        List<byte[]> result = jdbcTemplate.query("SELECT image FROM images WHERE id_image = ?", new Object[]{id}, ROW_MAPPER);
        return result.size() > 0 ? new Image(result.get(0)): null;
    }
}
