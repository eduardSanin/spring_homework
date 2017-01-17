package com.epam.cdp.sanin.controllers.facade;

import facade.interfaces.BookingFacade;
import model.impl.UserAccount;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class converts Object to List<String> where each entry of the list is a property from the object
 * i know, that it could be accomplished in an easier way, but it`s already written (
 */
@Component
public class DefaultExcelBookingFacade implements ExcelBookingFacade {
    @Autowired
    private Converter<Event, List<String>> eventConverter;
    @Autowired
    private Converter<User, List<String>> userConverter;
    @Autowired
    private Converter<Ticket, List<String>> ticketConverter;
    @Autowired
    private BookingFacade bookingFacade;


    @Override
    public List<List<String>>  getDefaultEvent() {
        List<String> result = eventConverter.convert(bookingFacade.getDefaultEvent().orElse(null));
        List<List<String>> results = new ArrayList<>();
        results.add(result);
        return results;
    }

    @Override
    public List<List<String>>  getDefaultUser() {
        List<String> result =  userConverter.convert(bookingFacade.getDefaultUser().orElse(null));
        List<List<String>> results = new ArrayList<>();
        results.add(result);
        return results;
    }

    @Override
    public List<List<String>>  getEventById(long id) {
        List<String> result = eventConverter.convert(bookingFacade.getEventById(id));
        List<List<String>> results = new ArrayList<>();
        results.add(result);
        return results;
    }

    @Override
    public List<List<String>> getEventsByTitle(String title, int pageSize, int pageNum) {
        return bookingFacade.getEventsByTitle(title, pageSize, pageNum).stream().map(eventConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<List<String>> getEventsForDay(Date day, int pageSize, int pageNum) {
        return bookingFacade.getEventsForDay(day, pageSize, pageNum).stream().map(eventConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<List<String>>  getUserById(long userId) {
        List<String> result = userConverter.convert(bookingFacade.getUserById(userId));
        List<List<String>> results = new ArrayList<>();
        results.add(result);
        return results;
    }

    @Override
    public List<List<String>> getUserByEmail(String email) {
        List<String> result = userConverter.convert(bookingFacade.getUserByEmail(email));
        List<List<String>> results = new ArrayList<>();
        results.add(result);
        return results;
    }

    @Override
    public List<List<String>> getUsersByName(String name, int pageSize, int pageNum) {
        return bookingFacade.getUsersByName(name, pageSize, pageNum).stream().map(userConverter::convert).collect(Collectors.toList());
    }


    @Override
    public List<List<String>> getBookedTickets(User user, int pageSize, int pageNum) {
        return bookingFacade.getBookedTickets(user, pageSize, pageNum).stream().map(ticketConverter::convert).collect(Collectors.toList());
    }

    @Override
    public List<List<String>> getBookedTickets(Event event, int pageSize, int pageNum) {
        return bookingFacade.getBookedTickets(event, pageSize, pageNum).stream().map(ticketConverter::convert).collect(Collectors.toList());
    }

}
