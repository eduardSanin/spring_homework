package com.epam.cdp.sanin.controllers.facade;

import model.interfaces.Event;
import model.interfaces.User;

import java.util.Date;
import java.util.List;

/**
 * this interface converts Object to List<String> where each entry of the list is a property from the object
 * i know, that it could be accomplished in an easier way, but it`s already written (
 */
public interface ExcelBookingFacade {
    List<List<String>>  getDefaultEvent();

    List<List<String>>  getDefaultUser();

    List<List<String>>  getEventById(long id);

    List<List<String>> getEventsByTitle(String title, int pageSize, int pageNum);

    List<List<String>> getEventsForDay(Date day, int pageSize, int pageNum);

    List<List<String>>  getUserById(long userId);

    List<List<String>>  getUserByEmail(String email);

    List<List<String>> getUsersByName(String name, int pageSize, int pageNum);

    List<List<String>> getBookedTickets(User user, int pageSize, int pageNum);

    List<List<String>> getBookedTickets(Event event, int pageSize, int pageNum);
}
