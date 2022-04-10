package ar.edu.itba.paw.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryJdbcDao implements CategoryDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<String> ROW_MAPPER = (rs, rowNum) -> rs.getString("category");

    @Autowired
    public CategoryJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<String> getCategories() {
        return jdbcTemplate.query("SELECT * FROM Categories", ROW_MAPPER);
    }
}
