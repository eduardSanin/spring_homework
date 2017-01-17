package dao.impl;

import dao.interfaces.EventDao;
import model.impl.EventEntry;
import model.interfaces.Event;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DefaultEventDao implements EventDao {
    private static final String DATE = "date";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String PRICE = "ticketPrice";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUM = "pageNum";
    private static final String EVENT = "event";

    private static final RowMapper<Event> EVENT_ROW_MAPPER = (resultSet, i) -> {
        Event event = new EventEntry();
        event.setDate(resultSet.getDate(DATE));
        event.setId(resultSet.getInt(ID));
        event.setTitle(resultSet.getString(TITLE));
        event.setTicketPrice(resultSet.getBigDecimal(PRICE));
        return event;
    };

    private static final Logger LOG = Logger.getLogger(DefaultEventDao.class);

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DefaultEventDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Event getEventById(long id) {
        Map params = new HashMap();
        params.put(ID, id);
        try {
            return jdbcTemplate.queryForObject("select * from " + EVENT + " where " + ID + " = :" + ID, params, EVENT_ROW_MAPPER);
        } catch (DataAccessException e) {
            LOG.error("no event object found with id " + id);
            return null;
        }
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Map params = new HashMap();
        params.put(TITLE, title);
        params.put(PAGE_SIZE, pageSize);
        params.put(PAGE_NUM, pageNum);
        return jdbcTemplate.query("select * from " + EVENT + " where " + TITLE + " = :" + TITLE + " LIMIT :" + PAGE_NUM + ", :" + PAGE_SIZE, params, EVENT_ROW_MAPPER);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Map params = new HashMap();
        params.put(DATE, day);
        params.put(PAGE_SIZE, pageSize);
        params.put(PAGE_NUM, pageNum);
        return jdbcTemplate.query("select * from " + EVENT + " where DAY(" + DATE + ") = DAY(:" + DATE + ") LIMIT :" + PAGE_NUM + ", :" + PAGE_SIZE, params, EVENT_ROW_MAPPER);
    }

    @Override
    public Event updateEvent(Event event) {
        Map params = new HashMap();
        params.put(DATE, event.getDate());
        params.put(TITLE, event.getTitle());
        params.put(PRICE, event.getTicketPrice());
        params.put(ID, event.getId());
        if (jdbcTemplate.update("update " + EVENT + " set " + TITLE + "=:" + TITLE + "," + PRICE + "=:" + PRICE + "," + DATE + "=:" + DATE + " where " + ID + "=:" + ID, params) > 0) {
            return event;
        }
        return null;
    }

    @Override
    public Event create(Event event) {
        KeyHolder holder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters
                .addValue(DATE, event.getDate())
                .addValue(TITLE, event.getTitle())
                .addValue(PRICE, event.getTicketPrice());
        if (jdbcTemplate.update("insert into " + EVENT + " (" + DATE + "," + TITLE + "," + PRICE + ") values(:" + DATE + ",:" + TITLE + ",:" + PRICE + ")", parameters, holder) > 0) {
            event.setId(holder.getKey().longValue());
            return event;
        }
        return null;
    }

    @Override
    public boolean remove(Long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID, id);
        return jdbcTemplate.update("delete from " + EVENT + " where " + ID + " = :" + ID, parameters) > 0;
    }

}
