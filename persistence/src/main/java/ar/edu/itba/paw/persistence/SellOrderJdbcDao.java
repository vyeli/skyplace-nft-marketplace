package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.SellOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class SellOrderJdbcDao implements SellOrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public SellOrderJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("SellOrders")
                .usingGeneratedKeyColumns("orderId");

//        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS SellOrders (" +
//                "orderId SERIAL PRIMARY KEY," +
//                "name VARCHAR(100) UNIQUE NOT NULL," +
//                "price NUMERIC(5) NOT NULL," +
//                "description VARCHAR(200)," +
//                "image bytea NOT NULL," +
//                "email VARCHAR(100) NOT NULL)"
//        );
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return Optional.empty();
    }

    @Override
    public SellOrder create(String name, double price, String description, byte[] image, String email) {
        return null;
    }
}
