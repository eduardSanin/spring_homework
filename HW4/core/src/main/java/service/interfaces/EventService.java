package service.interfaces;

import general.CrudInterface;
import model.interfaces.Event;

import java.util.Date;
import java.util.List;

public interface EventService extends CrudInterface<Event, Long> {
    Event getEventById(long id);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);

    Event updateEvent(Event event);
}
