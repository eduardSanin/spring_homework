package com.epam.cdp.sanin.controllers;

import facade.interfaces.BookingFacade;
import model.impl.*;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.*;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class BookingController {
    private static Logger LOG = Logger.getLogger(BookingController.class);

    @Autowired
    private BookingFacade bookingFacade;

    @RequestMapping(value = "/events/default", method = RequestMethod.GET)
    @ResponseBody
    public Event getDefaultEvent(HttpServletResponse response) {
        return bookingFacade.getDefaultEvent().orElseGet(() -> null);
    }

    @RequestMapping(value = "/events/default/{eventId}", method = RequestMethod.POST)
    public void setDefaultEvent(@PathVariable long eventId, HttpServletResponse response) {
        Event defaultEvent = bookingFacade.getEventById(eventId);
        bookingFacade.setDefaultEvent(defaultEvent);
        response.setStatus(SC_CREATED);
    }

    @RequestMapping(value = "/users/default", method = RequestMethod.GET)
    @ResponseBody
    public User getDefaultUser(HttpServletResponse response) {
        return bookingFacade.getDefaultUser().orElseGet(() -> null);
    }

    @RequestMapping(value = "/users/default/{userId}", method = RequestMethod.POST)
    public void setDefaultUser(@PathVariable long userId, HttpServletResponse response) {
        LOG.info(MessageFormat.format("setting default user to [{0}] ", userId));
        User defaultUser = bookingFacade.getUserById(userId);
        bookingFacade.setDefaultUser(defaultUser);
        response.setStatus(SC_CREATED);
    }

    @RequestMapping(value = "/events/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Event getEventById(@PathVariable long id, HttpServletResponse response) {
        return Optional.ofNullable(bookingFacade.getEventById(id)).orElseGet(() -> null);
    }

    @RequestMapping(value = "/events/title/{title}", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> getEventsByTitle(@PathVariable String title, @RequestParam int pageSize, @RequestParam int pageNum, HttpServletResponse response) {
        return Optional.ofNullable(bookingFacade.getEventsByTitle(title, pageSize, pageNum)).orElseGet(() -> null);
    }

    @RequestMapping(value = "/events/day/{day}", method = RequestMethod.GET)
    @ResponseBody
    public List<Event> getEventsForDay(@PathVariable Date day, @RequestParam int pageSize, @RequestParam int pageNum, HttpServletResponse response) {
        return Optional.ofNullable(bookingFacade.getEventsForDay(day, pageSize, pageNum)).orElseGet(() -> null);
    }

    @RequestMapping(value = "/events", method = RequestMethod.POST)
    @ResponseBody
    public Event createEvent(@RequestBody Event event, HttpServletResponse response) {
        response.setStatus(SC_CREATED);
        return Optional.ofNullable(bookingFacade.createEvent(event)).orElseGet(() -> null);
    }

    @RequestMapping(value = "/events", method = RequestMethod.PUT)
    @ResponseBody
    public Event updateEvent(@RequestBody Event event) {
        return bookingFacade.updateEvent(event);
    }

    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteEvent(@PathVariable long eventId, HttpServletResponse response) {
        return bookingFacade.deleteEvent(eventId);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public User getUserById(@PathVariable long userId, HttpServletResponse response) {
        return Optional.ofNullable(bookingFacade.getUserById(userId)).orElseGet(() -> null);
    }

    @RequestMapping(value = "/users", params = {"email"}, method = RequestMethod.GET)
    @ResponseBody
    public User getUserByEmail(@RequestParam String email, HttpServletResponse response) {
        return Optional.ofNullable(bookingFacade.getUserByEmail(email)).orElseGet(() -> null);
    }

    @RequestMapping(value = "/users", params = {"name", "pageSize", "pageNum"}, method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsersByName(@RequestParam String name, @RequestParam int pageSize, @RequestParam int pageNum) {
        return bookingFacade.getUsersByName(name, pageSize, pageNum);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseBody
    public User createUser(@RequestBody UserEntry user) {
        return bookingFacade.createUser(user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public User updateUser(@RequestBody UserEntry user) {
        return bookingFacade.updateUser(user);
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteUser(@PathVariable long userId, HttpServletResponse response) {
        return bookingFacade.deleteUser(userId);
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.POST, produces = {"application/pdf;charset=UTF-8", "application/json;charset=UTF-8"})
    @ResponseBody
    public Ticket bookTicket(@RequestParam long userId, @RequestParam long eventId, @RequestParam int place, @RequestParam String category, HttpServletResponse response) {
        return Optional.ofNullable(bookingFacade.bookTicket(userId, eventId, place, Ticket.Category.valueOf(category))).orElseGet(() -> null);
    }

    @RequestMapping(value = "/tickets", method = RequestMethod.GET, produces = {"application/pdf;charset=UTF-8", "application/json;charset=UTF-8"}, params = {"userId", "pageSize", "pageNum"})
    @ResponseBody
    public List<Ticket> getBookedTickets(@RequestParam int userId, @RequestParam int pageSize, @RequestParam int pageNum) {
        User user = new UserEntry();
        user.setId(userId);
        return bookingFacade.getBookedTickets(user, pageSize, pageNum);
    }

    @RequestMapping(value = "/tickets/event", method = RequestMethod.GET, produces = {"application/pdf;charset=UTF-8", "application/json;charset=UTF-8"})
    @ResponseBody
    public List<Ticket> getBookedTickets(@RequestBody EventEntry event, @RequestParam int pageSize, @RequestParam int pageNum) {
        return bookingFacade.getBookedTickets(event, pageSize, pageNum);
    }

    @RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.DELETE, produces = {"application/pdf;charset=UTF-8", "application/json;charset=UTF-8"})
    @ResponseBody
    public boolean cancelTicket(@PathVariable long ticketId, HttpServletResponse response) {
        return bookingFacade.cancelTicket(ticketId);
    }

    @RequestMapping(value = "/user/account", method = RequestMethod.POST)
    @ResponseBody
    public void refillingAccount(@RequestBody UserAccount userAccount) {
        bookingFacade.refillingAccount(userAccount);
    }


    @RequestMapping(value = "/pdf/tickets/user/{userId}", method = RequestMethod.GET, produces = {"application/pdf;charset=UTF-8"}, params = {"pageSize", "pageNum"})
    @ResponseBody
    public Tickets getBookedTicketsPdf(@PathVariable int userId, @RequestParam int pageSize, @RequestParam int pageNum) {
        User user = new UserEntry();
        user.setId(userId);
        List bookedTickets = bookingFacade.getBookedTickets(user, pageSize, pageNum);
        Tickets tickets = new Tickets();
        tickets.setTicketEntries(bookedTickets);
        return tickets;
    }

    @RequestMapping(value = "/pdf/tickets/event/{eventId}", method = RequestMethod.GET, produces = {"application/pdf;charset=UTF-8", "application/json;charset=UTF-8"}, params = {"pageSize", "pageNum"})
    @ResponseBody
    public Tickets getBookedTicketsAsPdf(@PathVariable long eventId, @RequestParam int pageSize, @RequestParam int pageNum) {
        Event event = new EventEntry();
        event.setId(eventId);
        Tickets tickets = new Tickets();
        List list = bookingFacade.getBookedTickets(event, pageSize, pageNum);
        List<TicketEntry> ticketEntries = new ArrayList<>();
        list.stream().forEach(o -> ticketEntries.add((TicketEntry) o));
        tickets.setTicketEntries(ticketEntries);
        return tickets;
    }

    @RequestMapping(value = "/Hateoas/users/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public Resource<User> getUserByIdHateoas(@PathVariable long userId) {
        Resource<User> userResource = new Resource<>(bookingFacade.getUserById(userId));
        userResource.add(linkTo(methodOn(BookingController.class).getUserByIdHateoas(userId)).withSelfRel());
        return userResource;
    }

    @RequestMapping(value = "/Hateoas/users/email/{email}", method = RequestMethod.GET)
    @ResponseBody
    public Resource<User> getUserByEmailHateoas(@PathVariable String email) {
        Resource<User> userResource = new Resource<>(bookingFacade.getUserByEmail(email));
        userResource.add(linkTo(methodOn(BookingController.class).getUserByEmailHateoas(email)).withSelfRel());
        return userResource;
    }

}
