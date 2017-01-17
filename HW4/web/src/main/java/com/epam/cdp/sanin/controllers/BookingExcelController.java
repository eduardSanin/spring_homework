package com.epam.cdp.sanin.controllers;

import com.epam.cdp.sanin.controllers.facade.ExcelBookingFacade;
import com.google.common.collect.Lists;
import model.interfaces.Event;
import model.interfaces.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Date;

/**
 * this controller uses custom excel view resolver
 */
@Controller
@RequestMapping(params = {"visual=true"})
public class BookingExcelController {
    private static Logger LOG = Logger.getLogger(BookingController.class);
    private static String EXCEL_VIEW = "excelView";
    private static String HEADER_ROWS = "headerRowsName";
    private static String DATA_COLUMNS = "dataColumns";
    @Autowired
    private ExcelBookingFacade excelBookingFacade;

    /**
     * excel file with user
     */
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public String getUserById(@PathVariable long userId, ModelMap model) {
        LOG.info(MessageFormat.format("trying to get user with id [{0}]", userId));
        model.put(HEADER_ROWS, Lists.newArrayList("user id", "user name", "user email"));
        model.put(DATA_COLUMNS, excelBookingFacade.getUserById(userId));
        return EXCEL_VIEW;
    }

    /**
     * excel file with event
     */
    @RequestMapping(value = "/events/default", method = RequestMethod.GET)
    public String getDefaultEvent(ModelMap model) {
        LOG.info("getting default event");
        model.put(HEADER_ROWS, Lists.newArrayList("default event"));
        model.put(DATA_COLUMNS, excelBookingFacade.getDefaultEvent());
        return EXCEL_VIEW;
    }

    /**
     * excel file with user
     */
    @RequestMapping(value = "/users/default", method = RequestMethod.GET)
    public String getDefaultUser(ModelMap model) {
        LOG.info("getting default user");
        model.put(HEADER_ROWS, Lists.newArrayList("default user"));
        model.put(DATA_COLUMNS, excelBookingFacade.getDefaultUser());
        return EXCEL_VIEW;
    }

    @RequestMapping(value = "/events/{id}", method = RequestMethod.GET)
    public String getEventById(@PathVariable long id, ModelMap model) {
        LOG.info(MessageFormat.format("event with id [{0}] was requested ", id));
        model.put(HEADER_ROWS, Lists.newArrayList("event header1", "event header2", "event header3"));
        model.put(DATA_COLUMNS, excelBookingFacade.getEventById(id));
        return EXCEL_VIEW;
    }

    @RequestMapping(value = "/events/title/{title}", method = RequestMethod.GET)
    public String getEventsByTitle(@PathVariable String title, @RequestParam int pageSize, @RequestParam int pageNum, ModelMap model) {
        LOG.info(MessageFormat.format("events with title [{0}] was requested ", title));
        model.put(HEADER_ROWS, Lists.newArrayList("event header1", "event header2", "event header3"));
        model.put(DATA_COLUMNS, excelBookingFacade.getEventsByTitle(title, pageSize, pageNum));
        return EXCEL_VIEW;
    }

    @RequestMapping(value = "/events/day/{day}", method = RequestMethod.GET)
    public String getEventsForDay(@PathVariable Date day, @RequestParam int pageSize, @RequestParam int pageNum, ModelMap model) {
        LOG.info(MessageFormat.format("events with day [{0}] was requested ", day.getDay()));
        model.put(HEADER_ROWS, Lists.newArrayList("event header1", "event header2", "event header3"));
        model.put(DATA_COLUMNS, excelBookingFacade.getEventsForDay(day, pageSize, pageNum));
        return EXCEL_VIEW;
    }

    @RequestMapping(value = "/users", params = {"email"}, method = RequestMethod.GET)
    public String getUserByEmail(@RequestParam String email, ModelMap model) {
        LOG.info(MessageFormat.format("trying to get user with email [{0}]", email));
        model.put(HEADER_ROWS, Lists.newArrayList("user id", "user name", "user email"));
        model.put(DATA_COLUMNS, excelBookingFacade.getUserByEmail(email));
        return EXCEL_VIEW;
    }

    @RequestMapping(value = "/users", params = {"name", "pageSize", "pageNum"}, method = RequestMethod.GET)
    public String getUsersByName(@RequestParam String name, @RequestParam int pageSize, @RequestParam int pageNum, ModelMap model) {
        LOG.info(MessageFormat.format("getting users with name [{0}]", name));
        model.put(HEADER_ROWS, Lists.newArrayList("user id", "user name", "user email"));
        model.put(DATA_COLUMNS, excelBookingFacade.getUsersByName(name, pageSize, pageNum));
        return EXCEL_VIEW;
    }

    @RequestMapping(value = "/tickets/user/", method = RequestMethod.GET)
    public String getBookedTickets(@RequestBody User user, @RequestParam int pageSize, @RequestParam int pageNum, ModelMap model) {
        LOG.info(MessageFormat.format("getting booked tickets by user [{0}]", user));
        model.put(HEADER_ROWS, Lists.newArrayList("id", "event id", "user id", "place", "category"));
        model.put(DATA_COLUMNS, excelBookingFacade.getBookedTickets(user, pageSize, pageNum));
        return EXCEL_VIEW;
    }

    @RequestMapping(value = "/tickets/event/", method = RequestMethod.GET)
    public String getBookedTickets(@RequestBody Event event, @RequestParam int pageSize, @RequestParam int pageNum, ModelMap model) {
        LOG.info(MessageFormat.format("getting booked tickets with event [{0}]", event));
        model.put(HEADER_ROWS, Lists.newArrayList("id", "event id", "user id", "place", "category"));
        model.put(DATA_COLUMNS, excelBookingFacade.getBookedTickets(event, pageSize, pageNum));
        return EXCEL_VIEW;
    }
}
