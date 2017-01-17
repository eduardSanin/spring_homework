import dao.impl.DefaultTicketDao;
import dao.impl.DefaultUserAccountDao;
import dao.interfaces.EventDao;
import model.impl.UserAccount;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import service.impl.DefaultTicketService;
import service.impl.DefaultUserAccountService;
import service.interfaces.TicketService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTicketServiceTest {
    private static final long USER_ID = 1;
    private static final long EVENT_ID = 1;
    private static final long TICKET_ID = 1;
    private static final int PLACE = 1;
    private static final int PAGE_SIZE = 3;
    private static final int PAGE_NUM = 1;
    private static final Ticket.Category CATEGORY = Ticket.Category.BAR;

    @Mock
    private EventDao eventDao;
    @Mock
    private DefaultUserAccountDao defaultUserAccountDao;
    @Mock
    private DefaultTicketDao ticketDao;
    @Mock
    private Ticket ticket;
    @Mock
    private List<Ticket> tickets;
    @Mock
    private User user;
    @Mock
    private Event event;
    @Mock
    private UserAccount userAccount;
    @Spy
    private DefaultUserAccountService defaultUserAccountService;

    @InjectMocks
    private TicketService ticketService = new DefaultTicketService();

    @Before
    public void setUp() throws Exception {
        when(defaultUserAccountDao.getUserAccountByUserId(USER_ID)).thenReturn(userAccount);
        when(userAccount.getPrePaidAmount()).thenReturn(BigDecimal.TEN);
        when(eventDao.getEventById(EVENT_ID)).thenReturn(event);
        when(event.getTicketPrice()).thenReturn(BigDecimal.TEN);
    }

    @Test
    public void shouldReturnBockedTicketsForGivenUser_whenGetBookedTickets() {
        when(ticketDao.getBookedTickets(user, PAGE_SIZE, PAGE_NUM)).thenReturn(tickets);
        List<Ticket> actual = ticketService.getBookedTickets(user, PAGE_SIZE, PAGE_NUM);
        assertEquals(tickets, actual);
    }

    @Test
    public void shouldReturnBockedTicketsForGivenEvent_whenGetBookedTickets() {
        when(ticketDao.getBookedTickets(event, PAGE_SIZE, PAGE_NUM)).thenReturn(tickets);
        List<Ticket> actual = ticketService.getBookedTickets(event, PAGE_SIZE, PAGE_NUM);
        assertEquals(tickets, actual);
    }

    @Test
    public void shouldDelegateCallToTicketDao_whenCancelTicket() {
        ticketService.cancelTicket(TICKET_ID);
        verify(ticketDao).cancelTicket(TICKET_ID);
    }
}