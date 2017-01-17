import dao.interfaces.UserAccountDao;
import facade.interfaces.BookingFacade;
import model.impl.EventEntry;
import model.impl.UserAccount;
import model.impl.UserEntry;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static model.interfaces.Ticket.Category;
import static org.fest.assertions.Assertions.assertThat;

@ActiveProfiles("h2")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-context.xml"})
@Transactional
@Sql("classpath:init.sql")
public class DefaultBookingFacadeIntegrationTest {
    @Autowired
    private BookingFacade bookingFacade;
    @Autowired
    private UserAccountDao userAccountDao;

    @Before
    public void setUp() throws Exception {
        bookingFacade.setDefaultEvent(null);
        bookingFacade.setDefaultUser(null);
    }

    @Test
    public void shouldUnMarshalTwoTicketsFromXmlFile() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed2323341drd@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        EventEntry eventEntry1 = new EventEntry();
        eventEntry1.setTitle("event1");
        eventEntry1.setDate(new Date());
        eventEntry1.setTicketPrice(BigDecimal.ONE);
        Event createdEvent1 = bookingFacade.createEvent(eventEntry1);

        EventEntry eventEntry2 = new EventEntry();
        eventEntry2.setTitle("event2");
        eventEntry2.setDate(new Date());
        eventEntry2.setTicketPrice(BigDecimal.ONE);
        Event createdEvent2 = bookingFacade.createEvent(eventEntry2);

        bookingFacade.loadTicketsFromXml("tickets.xml");

        List<Ticket> returnedTickets = bookingFacade.getBookedTickets(createdUser, 4, 0);
        assertThat(returnedTickets).hasSize(2);
    }

    @Test
    public void shouldCreateEvent() {
        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("event");
        eventEntry.setDate(new Date());
        eventEntry.setTicketPrice(BigDecimal.ONE);
        Event createdEvent = bookingFacade.createEvent(eventEntry);
        assertThat(createdEvent.getId()).isGreaterThan(0);
        assertThat(bookingFacade.getEventById(createdEvent.getId())).isEqualTo(createdEvent);
    }

    @Test
    public void shouldUpdateEvent() {
        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("event");
        eventEntry.setDate(new Date());
        eventEntry.setTicketPrice(BigDecimal.ONE);
        Event createdEvent = bookingFacade.createEvent(eventEntry);
        assertThat(createdEvent.getId()).isGreaterThan(0);
        assertThat(bookingFacade.getEventById(createdEvent.getId())).isNotNull();
        createdEvent.setTitle("newTitle");
        createdEvent.setTicketPrice(BigDecimal.TEN);
        createdEvent.setDate(new Date());
        assertThat(bookingFacade.updateEvent(createdEvent)).isNotNull();
        assertThat(bookingFacade.getEventById(createdEvent.getId())).isEqualTo(createdEvent);
    }

    @Test
    public void shouldDeleteEvent() {
        EventEntry eventEntry = new EventEntry();
        eventEntry.setDate(new Date());
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setTitle("test");
        Event createdEvent = bookingFacade.createEvent(eventEntry);
        assertThat(bookingFacade.getEventById(createdEvent.getId())).isEqualTo(createdEvent);
        assertThat(bookingFacade.deleteEvent(createdEvent.getId())).isTrue();
        assertThat(bookingFacade.getEventById(createdEvent.getId())).isNull();
    }

    @Test
    public void shouldCreateUser() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("email2yu1@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);
        assertThat(bookingFacade.getUserById(createdUser.getId())).isEqualTo(userEntry);
    }

    @Test
    public void shouldUpdateUser() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("email223yu1@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);
        assertThat(bookingFacade.getUserById(createdUser.getId())).isEqualTo(userEntry);
        String testEmail = "testEma23il@mail.ru";
        createdUser.setEmail(testEmail);
        bookingFacade.updateUser(createdUser);
        assertThat(bookingFacade.getUserById(createdUser.getId())).isEqualTo(createdUser);
    }


    @Test
    public void shouldDeleteUser() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("emai4326fl@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);
        assertThat(bookingFacade.getUserById(createdUser.getId())).isEqualTo(createdUser);
        assertTrue(bookingFacade.deleteUser(createdUser.getId()));
        assertNull(bookingFacade.getUserById(createdUser.getId()));
    }

    @Test
    public void shouldBookTicket() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed23d2423423423@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);

        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("test");
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(eventEntry);

        Ticket bookedTicket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 12, Category.BAR);

        List<Ticket> returnedList = bookingFacade.getBookedTickets(createdEvent, 4, 0);
        assertThat(returnedList).hasSize(1);
        assertThat(returnedList).containsExactly(bookedTicket);
    }

    @Test
    public void shouldSubtractManyFromUserAccount_whenBookTicket() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed23d2423423423@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);

        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("test");
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(eventEntry);

        bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 12, Category.BAR);
        assertThat(userAccountDao.getUserAccountByUserId(createdUser.getId()).getPrePaidAmount()).isEqualByComparingTo(BigDecimal.valueOf(9.00));
    }

    @Test
    public void shouldNotSubtractManyFromUserAccount_whenEventDoesNotExist() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed23d2423423423@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);

        bookingFacade.bookTicket(createdUser.getId(), -12312, 12, Category.BAR);
        assertThat(userAccountDao.getUserAccountByUserId(createdUser.getId()).getPrePaidAmount()).isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test
    public void shouldReturnTwoBookedTicketsByEvent() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed2323341drd@mail.ru");
        UserEntry userEntry2 = new UserEntry();
        userEntry2.setName("test2");
        userEntry2.setEmail("ed234234234234234234234d2@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);
        User createdUser2 = bookingFacade.createUser(userEntry2);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);
        userAccount.setId(-1);
        userAccount.setUserId(createdUser2.getId());
        bookingFacade.refillingAccount(userAccount);

        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("test");
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(eventEntry);

        Ticket bookedTicket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 12, Category.BAR);
        Ticket bookedTicket2 = bookingFacade.bookTicket(createdUser2.getId(), createdEvent.getId(), 13, Category.BAR);
        assertThat(bookedTicket).isNotNull();
        assertThat(bookedTicket2).isNotNull();
        assertThat(bookedTicket.getId()).isNotEqualTo(bookedTicket2.getId());

        List<Ticket> returnedList = bookingFacade.getBookedTickets(createdEvent, 4, 0);
        assertThat(returnedList).hasSize(2);
        assertThat(returnedList).containsExactly(bookedTicket, bookedTicket2);
    }

    @Test
    public void shouldReturnBookedTicketForDefaultUser_whenDefaultUserIsSpecified() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        UserEntry userEntry2 = new UserEntry();
        userEntry2.setName("defaultUser");
        userEntry2.setEmail("defaultUser@mail.ru");
        User createdDefaultUser = bookingFacade.createUser(userEntry2);

        assertThat(createdDefaultUser.getId()).isNotEqualTo(createdUser.getId());

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);
        userAccount.setId(createdDefaultUser.getId());
        bookingFacade.refillingAccount(userAccount);

        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("test");
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(eventEntry);
        bookingFacade.setDefaultUser(createdDefaultUser);

        Ticket bookedTicket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 12, Category.BAR);
        assertThat(bookedTicket.getUserId()).isEqualTo(createdUser.getId());
        List<Ticket> returnedList = bookingFacade.getBookedTickets(createdUser, 4, 0);
        assertThat(returnedList).isEmpty();
    }

    @Test
    public void shouldReturnBookedTicketForDefaultEvent_whenDefaultEventIsSpecified() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);

        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("test");
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(eventEntry);

        EventEntry eventEntry2 = new EventEntry();
        eventEntry2.setTitle("defaultEvent");
        eventEntry2.setTicketPrice(BigDecimal.ONE);
        eventEntry2.setDate(new Date());
        Event createdDefaultEvent = bookingFacade.createEvent(eventEntry2);

        bookingFacade.setDefaultEvent(createdDefaultEvent);

        Ticket bookedTicket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 12, Category.BAR);
        assertThat(bookedTicket.getEventId()).isEqualTo(createdEvent.getId());

        List<Ticket> returnedList = bookingFacade.getBookedTickets(createdEvent, 4, 0);
        assertThat(returnedList).isEmpty();
    }

    @Test
    public void shouldReturnTwoBookedTicketsByUser() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed4242342323d@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);

        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("test");
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(eventEntry);

        Ticket bookedTicket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 12, Category.BAR);
        Ticket bookedTicket2 = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 14, Category.PREMIUM);
        assertThat(bookedTicket).isNotNull();
        assertThat(bookedTicket2).isNotNull();
        assertThat(bookedTicket.getId()).isNotEqualTo(bookedTicket2.getId());

        List<Ticket> returnedList = bookingFacade.getBookedTickets(createdUser, 4, 0);
        assertThat(returnedList).hasSize(2);
        assertThat(returnedList).containsExactly(bookedTicket, bookedTicket2);
    }

    @Test
    public void shouldCancelTicket() {
        UserEntry userEntry = new UserEntry();
        userEntry.setName("test");
        userEntry.setEmail("ed23d@mail.ru");
        User createdUser = bookingFacade.createUser(userEntry);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(createdUser.getId());
        userAccount.setPrePaidAmount(BigDecimal.TEN);
        bookingFacade.refillingAccount(userAccount);

        EventEntry eventEntry = new EventEntry();
        eventEntry.setTitle("test");
        eventEntry.setTicketPrice(BigDecimal.ONE);
        eventEntry.setDate(new Date());
        Event createdEvent = bookingFacade.createEvent(eventEntry);

        Ticket bookedTicket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 12, Category.BAR);
        assertThat(bookingFacade.cancelTicket(bookedTicket.getId())).isTrue();
        assertThat(bookingFacade.getBookedTickets(eventEntry, 4, 0)).isEmpty();
    }

    @Test
    public void shouldReturnEvent_whenThereIsSuchEventWithId() {
        EventEntry eventEntry1 = new EventEntry();
        eventEntry1.setTitle("t");
        eventEntry1.setDate(new Date());
        eventEntry1.setTicketPrice(BigDecimal.ONE);
        Event createdEvent = bookingFacade.createEvent(eventEntry1);
        assertThat(bookingFacade.getEventById(eventEntry1.getId())).isEqualTo(createdEvent);
    }

    @Test
    public void shouldReturnNull_whenThereIsNoSuchEventWithSuchId() {
        assertThat(bookingFacade.getEventById(112)).isNull();
    }

    @Test
    public void shouldReturn2Events_whenThereIsSuchEventsWithTitle() {
        EventEntry eventEntry1 = new EventEntry();
        eventEntry1.setTitle("tttt");
        eventEntry1.setDate(new Date());
        eventEntry1.setTicketPrice(BigDecimal.ONE);
        EventEntry eventEntry2 = new EventEntry();
        eventEntry2.setTitle("tttt");
        eventEntry2.setDate(new Date());
        eventEntry2.setTicketPrice(BigDecimal.TEN);
        Event createdEvent1 = bookingFacade.createEvent(eventEntry1);
        Event createdEvent2 = bookingFacade.createEvent(eventEntry2);
        List<Event> returnedEvents = bookingFacade.getEventsByTitle(eventEntry1.getTitle(), 4, 0);
        assertThat(returnedEvents).hasSize(2);
        assertThat(returnedEvents).containsExactly(createdEvent1, createdEvent2);
    }

    @Test
    public void shouldReturn1Event_whenThereIsSuchEventWithTitleAndSkipModeIsEnabled() {
        EventEntry eventEntry1 = new EventEntry();
        eventEntry1.setTitle("t");
        eventEntry1.setDate(new Date());
        eventEntry1.setTicketPrice(BigDecimal.ONE);
        EventEntry eventEntry2 = new EventEntry();
        eventEntry2.setTitle("t");
        eventEntry2.setDate(new Date());
        eventEntry2.setTicketPrice(BigDecimal.TEN);
        bookingFacade.createEvent(eventEntry1);
        bookingFacade.createEvent(eventEntry2);
        List<Event> returnedList = bookingFacade.getEventsByTitle(eventEntry1.getTitle(), 4, 1);
        assertThat(returnedList).hasSize(1);
    }

    @Test
    public void shouldReturnEmptyList_whenThereIsNoSuchEventsWithTitle() {
        assertThat(bookingFacade.getEventsByTitle("noTitle", 4, 0)).isEmpty();
    }

    @Test
    public void shouldReturn2Events_whenThereIsSuchEventsWithTheSameDay() {
        EventEntry eventEntry1 = new EventEntry();
        eventEntry1.setTitle("ttttt");
        eventEntry1.setDate(new Date());
        eventEntry1.setTicketPrice(BigDecimal.ONE);
        EventEntry eventEntry2 = new EventEntry();
        eventEntry2.setTitle("ttttt2");
        eventEntry2.setDate(new Date());
        eventEntry2.setTicketPrice(BigDecimal.TEN);
        Event createdEvent1 = bookingFacade.createEvent(eventEntry1);
        Event createdEvent2 = bookingFacade.createEvent(eventEntry2);
        List<Event> returnedList = bookingFacade.getEventsForDay(eventEntry1.getDate(), 3, 0);
        assertThat(returnedList).hasSize(2);
        assertThat(returnedList).containsExactly(createdEvent1, createdEvent2);
    }

    @Test
    public void shouldReturnUserById() {
        UserEntry userEntry = new UserEntry();
        userEntry.setEmail("email2@mail.ru");
        userEntry.setName("eddd");
        User createdUser = bookingFacade.createUser(userEntry);
        assertThat(bookingFacade.getUserById(userEntry.getId())).isEqualTo(createdUser);
    }

    @Test
    public void shouldReturnUserByEmail() {
        UserEntry userEntry = new UserEntry();
        userEntry.setEmail("email");
        userEntry.setName("eds");
        User createdUser = bookingFacade.createUser(userEntry);
        assertThat(bookingFacade.getUserByEmail(userEntry.getEmail())).isEqualTo(createdUser);
    }

    @Test
    public void shouldReturn2UsersByName() {
        UserEntry userEntry1 = new UserEntry();
        userEntry1.setEmail("email323213@mail.ru");
        userEntry1.setName("edik");
        User createdUser1 = bookingFacade.createUser(userEntry1);
        UserEntry userEntry2 = new UserEntry();
        userEntry2.setEmail("email24234@mail.com");
        userEntry2.setName("edik");
        User createdUser2 = bookingFacade.createUser(userEntry2);
        List<User> returnedList = bookingFacade.getUsersByName(userEntry1.getName(), 4, 0);
        assertThat(returnedList).hasSize(2);
        assertThat(returnedList).contains(createdUser1, createdUser2);
    }
}