package facade.impl;

import facade.interfaces.BookingFacade;
import general.DefaultUserAndEventHolder;
import model.impl.UserAccount;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import service.interfaces.EventService;
import service.interfaces.TicketService;
import service.interfaces.UserAccountService;
import service.interfaces.UserService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class DefaultBookingFacade implements BookingFacade {
    private static final Logger LOG = Logger.getLogger(DefaultBookingFacade.class);
    private EventService eventService;
    private TicketService ticketService;
    private UserService userService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private DefaultUserAndEventHolder defaultUserAndEventHolder;

    public DefaultBookingFacade(EventService eventService, TicketService ticketService, UserService userService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @Override
    public void loadTicketsFromXml(String fileName) {
        ticketService.loadTicketsFromXml(fileName);
    }

    @Override
    public Optional<Event> getDefaultEvent() {
        return Optional.ofNullable(defaultUserAndEventHolder.getDefaultEvent());
    }

    @Override
    public void setDefaultEvent(Event event) {
        defaultUserAndEventHolder.setDefaultEvent(event);
    }

    @Override
    public Optional<User> getDefaultUser() {
        return Optional.ofNullable(defaultUserAndEventHolder.getDefaultUser());
    }

    @Override
    public void setDefaultUser(User user) {
        defaultUserAndEventHolder.setDefaultUser(user);
    }

    @Override
    public Event getEventById(long id) {
        LOG.info("trying to get event with id:" + id);
        return eventService.getEventById(id);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        LOG.info("fetching events with tile:" + title);
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOG.info("fetching events by day:" + day.getDay());
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        LOG.info("creating event: with id:" + event.getId());
        return eventService.create(event);
    }

    @Override
    public Event updateEvent(Event event) {
        LOG.info("update event with id:" + event.getId());
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        LOG.info("deleting event with id:" + eventId);
        return eventService.remove(eventId);
    }

    @Override
    public User getUserById(long userId) {
        LOG.info("trying to get user with id:" + userId);
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        LOG.info("trying to get user with email:" + email);
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        LOG.info("trying to get users by name: " + name);
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        LOG.info("creating user with id:" + user.getId());
        return userService.create(user);
    }

    @Override
    public User updateUser(User user) {
        LOG.info("updating user with id:" + user.getId());
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        LOG.info("deleting user with id: " + userId);
        return userService.remove(userId);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        LOG.info("booking ticket with id: " + userId);
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        LOG.info("trying to get tickets by user id: " + user.getId());
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        LOG.info("trying to get booked tickets by event.id: " + event.getId());
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        LOG.info("cancelling ticket with id: " + ticketId);
        return ticketService.cancelTicket(ticketId);
    }

    @Override
    public void refillingAccount(UserAccount userAccount) {
        LOG.info("refilling userAccount");
        userAccountService.refillingAccount(userAccount);
    }
}
