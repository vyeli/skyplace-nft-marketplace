package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.exceptions.CreateImageException;
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

    private static final RowMapper<Image> ROW_MAPPER = (rs, rowNum) ->  {
        byte[] bytes = rs.getBytes("image");
        return new Image(bytes);
    };

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertImage = new SimpleJdbcInsert(ds)
                .withTableName("images")
                .usingGeneratedKeyColumns("id_image");
    }

    @Override
    public int createImage(MultipartFile image) {
        try {
            byte[] bytes = image.getBytes();
            final Map<String, Object> imageData = new HashMap<>();
            imageData.put("image", bytes);
            return jdbcInsertImage.executeAndReturnKey(imageData).intValue();
        } catch(Exception e) {
            throw new CreateImageException();
        }
    }

    public Optional<Image> getImage(int id) {
        return jdbcTemplate.query("SELECT image FROM images WHERE id_image = ?", new Object[]{id}, ROW_MAPPER).stream().findFirst();
    }
}
