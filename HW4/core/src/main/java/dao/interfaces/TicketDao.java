package dao.interfaces;

import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;

import java.util.List;

public interface TicketDao {
    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);
}
