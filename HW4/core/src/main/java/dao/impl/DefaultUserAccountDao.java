package dao.impl;

import dao.interfaces.UserAccountDao;
import model.impl.UserAccount;
import model.impl.mongo.UserAcc;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
@Profile("!mongo")
public class DefaultUserAccountDao implements UserAccountDao {
    private static final String ID = "id";
    private static final String USER_ACCOUNT = "userAccount";
    private static final String PRE_PAID_AMOUNT = "prePaidAmount";
    private static final String USER_ID = "userId";
    private static final Logger LOG = Logger.getLogger(DefaultUserAccountDao.class);
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DefaultUserAccountDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public UserAcc createUserAccount(UserAcc userAccount) {
        KeyHolder holder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(PRE_PAID_AMOUNT, userAccount.getPrePaidAmount())
                .addValue(USER_ID, userAccount.getUserId());
        if (jdbcTemplate.update("insert into " + USER_ACCOUNT + " (" + PRE_PAID_AMOUNT + "," + USER_ID + ") values(:" + PRE_PAID_AMOUNT + ",:" + USER_ID + ")", parameters, holder) > 0) {
            userAccount.setId(holder.getKey().longValue());
            return userAccount;
        } else {
            return null;
        }
    }

    @Override
    public UserAccount getUserAccountByUserId(long userId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID, userId);
        try {
            return jdbcTemplate.queryForObject("select * from " + USER_ACCOUNT + " where " + ID + "=:" + ID, parameters, (resultSet, i) -> {
                UserAccount userAccount = new UserAccount();
                userAccount.setId(resultSet.getInt(ID));
                userAccount.setUserId(resultSet.getInt(USER_ID));
                userAccount.setPrePaidAmount(resultSet.getBigDecimal(PRE_PAID_AMOUNT));
                return userAccount;
            });
        } catch (DataAccessException e) {
            LOG.error(e);
            return null;
        }
    }

    @Override
    public boolean updateUserAccount(UserAcc userAccount) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID, userAccount.getId());
        parameters.addValue(PRE_PAID_AMOUNT, userAccount.getPrePaidAmount());
        return jdbcTemplate.update("update " + USER_ACCOUNT + " set " + PRE_PAID_AMOUNT + "=:" + PRE_PAID_AMOUNT + " where " + ID + "=:" + ID, parameters) > 0;
    }
}
