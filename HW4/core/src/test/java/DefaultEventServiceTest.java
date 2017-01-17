import dao.interfaces.EventDao;
import model.interfaces.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.impl.DefaultEventService;
import service.interfaces.EventService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultEventServiceTest {
    private static final String TITLE = "title";
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUM = 1;
    private static final long EVENT_ID = 1;
    private static final Date DAY = new Date();

    @Mock
    private EventDao eventDao;
    @Mock
    private Event event;
    @Mock
    private List<Event> events;
    @InjectMocks
    private EventService eventService = new DefaultEventService();

    @Test
    public void shouldReturnEventByGivenId_whenGetEventsByTitle() {
        when(eventDao.getEventById(EVENT_ID)).thenReturn(event);
        Event actual = eventService.getEventById(EVENT_ID);
        assertEquals(event, actual);
    }

    @Test
    public void shouldReturnEventsByGivenTitle_whenGetEventsByTitle() {
        when(eventDao.getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUM)).thenReturn(events);
        List<Event> actual = eventService.getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUM);
        assertEquals(events, actual);
    }

    @Test
    public void shouldReturnEventsByGivenDay_whenGetEventsByDay() {
        when(eventDao.getEventsForDay(DAY, PAGE_SIZE, PAGE_NUM)).thenReturn(events);
        List<Event> actual = eventService.getEventsForDay(DAY, PAGE_SIZE, PAGE_NUM);
        assertEquals(events, actual);
    }

    @Test
    public void shouldDelegateCallToEventDaoCreateEvent_whenCreateEvent() {
        eventService.create(event);
        verify(eventDao).create(event);
    }

    @Test
    public void shouldDelegateCallToEventDaoUpdateEvent_whenUpdateEvent() {
        eventService.updateEvent(event);
        verify(eventDao).updateEvent(event);
    }

    @Test
    public void shouldDelegateCallToEventDaoDeleteEvent_whenDeleteEvent() {
        eventService.remove(EVENT_ID);
        verify(eventDao).remove(EVENT_ID);
    }
}
