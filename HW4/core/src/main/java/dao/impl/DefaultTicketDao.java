package dao.impl;

import dao.interfaces.TicketDao;
import general.CustomMapSqlParameterSource;
import general.DefaultUserAndEventHolder;
import model.impl.TicketEntry;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DefaultTicketDao implements TicketDao {
    private static final String ID = "id";
    private static final String TICKET = "ticket";
    private static final String USER_ID = "uid";
    private static final String EVENT_ID = "eid";
    private static final String CATEGORY = "category";
    private static final String PLACE = "place";
    private static final String PAGE_SIZE = "pageSize";
    private static final String PAGE_NUM = "pageNum";
    private static final RowMapper<Ticket> TICKET_ROW_MAPPER = (resultSet, i) -> {
        Ticket ticket = new TicketEntry();
        ticket.setId(resultSet.getInt(ID));
        ticket.setEventId(resultSet.getInt(EVENT_ID));
        ticket.setUserId(resultSet.getInt(USER_ID));
        ticket.setPlace(resultSet.getInt(PLACE));
        ticket.setCategory(Ticket.Category.valueOf(resultSet.getString(CATEGORY)));
        return ticket;
    };

    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private DefaultUserAndEventHolder defaultUserAndEventHolder;

    @Autowired
    public DefaultTicketDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        Ticket ticket = new TicketEntry();
        ticket.setCategory(category);
        ticket.setPlace(place);
        ticket.setEventId(eventId);
        ticket.setUserId(userId);
        KeyHolder holder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(USER_ID, userId)
                .addValue(EVENT_ID, eventId)
                .addValue(PLACE, place);
        if (jdbcTemplate.update("insert into " + TICKET + " (" + USER_ID + "," + EVENT_ID + "," + PLACE + "," + CATEGORY + ") values(:" + USER_ID + ",:" + EVENT_ID + ",:" + PLACE + ",'" + category.toString() + "')", parameters, holder) > 0) {
            ticket.setId(holder.getKey().longValue());
            return ticket;
        }
        return null;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        CustomMapSqlParameterSource defaultUserLookUpParameterSource = new CustomMapSqlParameterSource(defaultUserAndEventHolder);
        defaultUserLookUpParameterSource
                .addValue(USER_ID, user.getId())
                .addValue(PAGE_NUM, pageNum)
                .addValue(PAGE_SIZE, pageSize);
        return jdbcTemplate.query("select * from " + TICKET + " inner join user on user.id = " + TICKET + ".uid" + " where user.id =:" + USER_ID + " LIMIT :" + PAGE_NUM + ",:" + PAGE_SIZE, defaultUserLookUpParameterSource, TICKET_ROW_MAPPER);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        CustomMapSqlParameterSource defaultUserLookUpParameterSource = new CustomMapSqlParameterSource(defaultUserAndEventHolder);
        defaultUserLookUpParameterSource
                .addValue(EVENT_ID, event.getId())
                .addValue(PAGE_NUM, pageNum)
                .addValue(PAGE_SIZE, pageSize);
        return jdbcTemplate.query("select * from " + TICKET + " inner join event on event.id = " + TICKET + ".eid" + " where event.id =:" + EVENT_ID + " LIMIT :" + PAGE_NUM + ",:" + PAGE_SIZE, defaultUserLookUpParameterSource, TICKET_ROW_MAPPER);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(ID, ticketId);
        return jdbcTemplate.update("delete from " + TICKET + " where " + ID + "=:" + ID, parameters) > 0;
    }
}
