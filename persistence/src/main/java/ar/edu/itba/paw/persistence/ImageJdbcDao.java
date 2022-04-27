package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Repository
public class ImageJdbcDao implements ImageDao{

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<byte[]> ROW_MAPPER = (rs, rowNum) -> rs.getBytes("image");

    @Autowired
    public ImageJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    public Image getImage(long id) {
        List<byte[]> result = jdbcTemplate.query("SELECT image FROM images WHERE id_image = ?", new Object[]{id}, ROW_MAPPER);
        System.out.println(Arrays.toString(result.get(0)));
        return result.size() > 0 ? new Image(result.get(0)): null;
    }
}
