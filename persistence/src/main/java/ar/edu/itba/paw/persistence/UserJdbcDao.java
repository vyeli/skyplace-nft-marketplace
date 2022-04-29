package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao{

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertSellOrder;

    private static final RowMapper<User> ROW_MAPPER = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("email"), rs.getString("username"), rs.getString("wallet"), "chain", rs.getString("password"));

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertSellOrder = new SimpleJdbcInsert(ds)
                .withTableName("Users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<User> create(String email, String username, String wallet, String password) {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("username", username);
        userData.put("wallet", wallet);
        userData.put("password", password);

        try {
            final long userId = jdbcInsertSellOrder.executeAndReturnKey(userData).longValue();
            return Optional.of(new User(userId, email, username, "chain", wallet, password));
        } catch (DuplicateKeyException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByEmail(final String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", new Object[]{ email }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> getUserById(final long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new Object[]{ id }, ROW_MAPPER).stream().findFirst();
    }
}
