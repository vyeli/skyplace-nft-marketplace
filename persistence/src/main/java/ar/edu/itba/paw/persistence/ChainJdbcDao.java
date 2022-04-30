package ar.edu.itba.paw.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChainJdbcDao implements ChainDao {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<String> ROW_MAPPER = (rs, rowNum) -> rs.getString("chain");

    @Autowired
    public ChainJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public List<String> getChains() {
        return jdbcTemplate.query("SELECT * FROM Chains ORDER BY chain ASC", ROW_MAPPER);
    }
}
