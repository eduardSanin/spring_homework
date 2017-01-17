package dao.impl;

import dao.interfaces.UserDao;
import model.impl.UserEntry;
import model.interfaces.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Profile("!mongo")
public class DefaultUserDao implements UserDao {
    private static final String USER = "user";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUM = "pageNum";
    private static final RowMapper<User> USER_ROW_MAPPER = (resultSet, i) -> {
        User user = new UserEntry();
        user.setId(resultSet.getInt(ID));
        user.setEmail(resultSet.getString(EMAIL));
        user.setName(resultSet.getString(NAME));
        return user;
    };
    private static Logger LOG = Logger.getLogger(DefaultUserDao.class);
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DefaultUserDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public User getUserById(long userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID, userId);
        try {
            return jdbcTemplate.queryForObject("select * from " + USER + " where " + ID + "=:" + ID, parameters, USER_ROW_MAPPER);
        } catch (DataAccessException e) {
            LOG.error(e);
            return null;
        }
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(NAME, name);
        parameters.addValue(PAGE_NUM, pageNum);
        parameters.addValue(PAGE_SIZE, pageNum);
        try {
            return jdbcTemplate.query("select * from " + USER + " where " + NAME + "=:" + NAME, parameters, USER_ROW_MAPPER);
        } catch (DataAccessException e) {
            LOG.error(e);
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(EMAIL, email);
        try {
            return jdbcTemplate.queryForObject("select * from " + USER + " where " + EMAIL + "=:" + EMAIL, parameters, USER_ROW_MAPPER);
        } catch (DataAccessException e) {
            LOG.error(e);
            return null;
        }
    }

    @Override
    public User updateUser(User user) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(NAME, user.getName())
                .addValue(EMAIL, user.getEmail())
                .addValue(ID, user.getId());
        if (jdbcTemplate.update("UPDATE " + USER + " set " + EMAIL + " =:" + EMAIL + "," + NAME + "=:" + NAME + " where " + ID + "=:" + ID, parameters) > 0) {
            return user;
        }
        return null;
    }

    @Override
    public User create(User user) {
        KeyHolder holder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(NAME, user.getName())
                .addValue(EMAIL, user.getEmail());
        if (jdbcTemplate.update("insert into " + USER + " (" + EMAIL + "," + NAME + ") values(:" + EMAIL + ",:" + NAME + ")", parameters, holder) > 0) {
            user.setId(holder.getKey().longValue());
            return user;
        }
        return null;
    }

    @Override
    public boolean remove(Long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID, id);
        return jdbcTemplate.update("delete from " + USER + " where " + ID + " = :" + ID, parameters) > 0;
    }
}
