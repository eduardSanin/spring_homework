import facade.impl.DefaultBookingFacade;
import facade.interfaces.BookingFacade;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.interfaces.EventService;
import service.interfaces.TicketService;
import service.interfaces.UserService;

import java.util.Date;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBookingFacadeTest {
    private static final long EVENT_ID = 1;
    private static final long USER_ID = 1;
    private static final long TICKET_ID = 1;
    private static final String TITLE = "title";
    private static final String EMAIL = "1@mail.com";
    private static final String NAME = "name";
    private static final int PAGE_SIZE = 5;
    private static final int PAGE_NUM = 1;
    private static final int PLACE = 1;
    private static final Date DAY = new Date();
    private static final Ticket.Category CATEGORY = Ticket.Category.BAR;

    @Mock
    private EventService eventService;
    @Mock
    private TicketService ticketService;
    @Mock
    private UserService userService;
    @Mock
    private Event event;
    @Mock
    private User user;
    @InjectMocks
    private BookingFacade bookingFacade = new DefaultBookingFacade(eventService, ticketService, userService);

    @Test
    public void shouldDelegateCallToEventServiceGetEventById_whenEventById() {
        bookingFacade.getEventById(EVENT_ID);
        verify(eventService).getEventById(EVENT_ID);
    }

    @Test
    public void shouldDelegateCallToEventServiceGetEventsByTitle_whenGetEventsByTitle() {
        bookingFacade.getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUM);
        verify(eventService).getEventsByTitle(TITLE, PAGE_SIZE, PAGE_NUM);
    }

    @Test
    public void shouldDelegateCallToEventServiceGetEventsForDay_whenGetEventsForDay() {
        bookingFacade.getEventsForDay(DAY, PAGE_SIZE, PAGE_NUM);
        verify(eventService).getEventsForDay(DAY, PAGE_SIZE, PAGE_NUM);
    }

    @Test
    public void shouldDelegateCallToEventServiceCreateEvent_whenCreateEvent() {
        bookingFacade.createEvent(event);
        verify(eventService).create(event);
    }

    @Test
    public void shouldDelegateCallToEventServiceUpdateEvent_whenUpdateEvent() {
        bookingFacade.updateEvent(event);
        verify(eventService).updateEvent(event);
    }

    @Test
    public void shouldDelegateCallToEventServiceDeleteEvent_whenDeleteEvent() {
        bookingFacade.deleteEvent(EVENT_ID);
        verify(eventService).remove(EVENT_ID);
    }

    @Test
    public void shouldDelegateCallToUserServiceGetUserById_whenGetUserById() {
        bookingFacade.getUserById(USER_ID);
        verify(userService).getUserById(USER_ID);
    }

    @Test
    public void shouldDelegateCallToUserServiceGetUserByEmail_whenGetUserByEmail() {
        bookingFacade.getUserByEmail(EMAIL);
        verify(userService).getUserByEmail(EMAIL);
    }

    @Test
    public void shouldDelegateCallToUserServiceGetUsersByName_whenGetUsersByName() {
        bookingFacade.getUsersByName(NAME, PAGE_SIZE, PAGE_NUM);
        verify(userService).getUsersByName(NAME, PAGE_SIZE, PAGE_NUM);
    }

    @Test
    public void shouldDelegateCallToUserServiceCreateUser_whenCreateUser() {
        bookingFacade.createUser(user);
        verify(userService).create(user);
    }

    @Test
    public void shouldDelegateCallToUserServiceUpdateUser_whenUpdateUser() {
        bookingFacade.updateUser(user);
        verify(userService).updateUser(user);
    }

    @Test
    public void shouldDelegateCallToUserServiceDeleteUser_whenDeleteUser() {
        bookingFacade.deleteUser(USER_ID);
        verify(userService).remove(USER_ID);
    }

    @Test
    public void shouldDelegateCallToTicketServiceBookTicket_whenBookTicket() {
        bookingFacade.bookTicket(USER_ID, TICKET_ID, PLACE, CATEGORY);
        verify(ticketService).bookTicket(USER_ID, TICKET_ID, PLACE, CATEGORY);
    }

    @Test
    public void shouldDelegateCallToTicketServiceGetBookedTickets_whenGetBookedTicketsForUser() {
        bookingFacade.getBookedTickets(user, PAGE_SIZE, PAGE_NUM);
        verify(ticketService).getBookedTickets(user, PAGE_SIZE, PAGE_NUM);
    }

    @Test
    public void shouldDelegateCallToTicketServiceGetBookedTickets_whenGetBookedTicketsForEvent() {
        bookingFacade.getBookedTickets(event, PAGE_SIZE, PAGE_NUM);
        verify(ticketService).getBookedTickets(event, PAGE_SIZE, PAGE_NUM);
    }

    @Test
    public void shouldDelegateCallToTicketServiceCancelTicket_whenCancelTicket() {
        bookingFacade.cancelTicket(TICKET_ID);
        verify(ticketService).cancelTicket(TICKET_ID);
    }

}