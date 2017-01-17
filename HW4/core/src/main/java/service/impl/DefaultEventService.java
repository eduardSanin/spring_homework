package service.impl;

import dao.interfaces.EventDao;
import model.interfaces.Event;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import service.interfaces.EventService;

import java.util.Date;
import java.util.List;

@Service
public class DefaultEventService implements EventService {
    private static final Logger LOG = Logger.getLogger(DefaultEventService.class);
    private EventDao eventDao;

    @Override
    public Event getEventById(long id) {
        return eventDao.getEventById(id);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventDao.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventDao.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventDao.updateEvent(event);
    }


    @Override
    public Event create(Event event) {
        return eventDao.create(event);
    }

    @Override
    public boolean remove(Long id) {
        return eventDao.remove(id);
    }

    @Required
    @Autowired
    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }
}
