package com.epam.cdp.sanin.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import facade.interfaces.BookingFacade;
import model.impl.TicketEntry;
import model.impl.UserEntry;
import model.interfaces.Event;
import model.interfaces.Ticket;
import model.interfaces.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:spring-core-app-context.xml",
        "classpath:excel-view-resolver-app-context.xml",
        "classpath:main-app-context.xml"
})
public class BookingControllerTest {
    private static final long EXPECTED_USER1_ID = 1;
    private static final long EXPECTED_USER2_ID = 2;
    private static final String EXPECTED_USER1_EMAIL = "eddihoc@mail.ru";
    private static final String EXPECTED_USER2_EMAIL = "eddihoc2@mail.ru";
    private static final String EXPECTED_USERS_NAME = "ed";
    private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    private static final long EXPECTED_TICKET1_ID = 1;
    private static final long EXPECTED_EVENT1_ID = 1;
    private static final int EXPECTED_TICKETS_PLACE = 2;
    private static final Ticket.Category EXPECTED_TICKETS_CATEGORY = Ticket.Category.BAR;
    private static final long EXPECTED_TICKET2_ID = 2;
    private static final long EXPECTED_EVENT2_ID = 2;
    private static final String APPLICATION_PDF_CHARSET_UTF_8 = "application/pdf";

    @InjectMocks
    private BookingController bookingControllerMock;
    @Mock
    private BookingFacade bookingFacadeMock;
    private MockMvc mockMvc;
    private User returnedUser1;
    private User returnedUser2;
    private Ticket ticket1;
    private Ticket ticket2;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(bookingControllerMock).build();
        initEntries();
        when(bookingFacadeMock.createUser(returnedUser1)).thenReturn(returnedUser2);
        when(bookingFacadeMock.updateUser(returnedUser1)).thenReturn(returnedUser2);
        when(bookingFacadeMock.deleteUser(returnedUser1.getId())).thenReturn(true);
        when(bookingFacadeMock.getUserById(EXPECTED_USER1_ID)).thenReturn(returnedUser1);
        when(bookingFacadeMock.getUserById(EXPECTED_USER2_ID)).thenReturn(returnedUser2);
        when(bookingFacadeMock.getUserByEmail(EXPECTED_USER1_EMAIL)).thenReturn(returnedUser1);
        when(bookingFacadeMock.getUserByEmail(EXPECTED_USER2_EMAIL)).thenReturn(returnedUser2);
        when(bookingFacadeMock.getUsersByName(EXPECTED_USERS_NAME,2,0)).thenReturn(Lists.newArrayList(returnedUser1,returnedUser2));
        when(bookingFacadeMock.bookTicket(EXPECTED_USER1_ID,EXPECTED_EVENT1_ID,EXPECTED_TICKETS_PLACE,EXPECTED_TICKETS_CATEGORY)).thenReturn(ticket1);
    }

    private void initEntries() {
        returnedUser1 = new UserEntry();
        returnedUser1.setId(EXPECTED_USER1_ID);
        returnedUser1.setEmail(EXPECTED_USER1_EMAIL);
        returnedUser1.setName(EXPECTED_USERS_NAME);

        returnedUser2 = new UserEntry();
        returnedUser2.setId(EXPECTED_USER2_ID);
        returnedUser2.setEmail(EXPECTED_USER2_EMAIL);
        returnedUser2.setName(EXPECTED_USERS_NAME);

        ticket1 = new TicketEntry();
        ticket1.setId(EXPECTED_TICKET1_ID);
        ticket1.setUserId(EXPECTED_USER1_ID);
        ticket1.setEventId(EXPECTED_EVENT1_ID);
        ticket1.setPlace(EXPECTED_TICKETS_PLACE);
        ticket1.setCategory(EXPECTED_TICKETS_CATEGORY);

        ticket2 = new TicketEntry();
        ticket2.setId(EXPECTED_TICKET2_ID);
        ticket2.setUserId(EXPECTED_USER2_ID);
        ticket2.setEventId(EXPECTED_EVENT2_ID);
        ticket2.setPlace(EXPECTED_TICKETS_PLACE);
        ticket2.setCategory(EXPECTED_TICKETS_CATEGORY);
    }

    @Test
    public void shouldReturnUserByEmailAsJson() throws Exception {
        mockMvc.perform(get("/users").param("email",returnedUser1.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.id", is((int)returnedUser1.getId())))
                .andExpect(jsonPath("$.email", equalTo(returnedUser1.getEmail())))
                .andExpect(jsonPath("$.name", equalTo(returnedUser1.getName())));
    }

    @Test
    public void shouldReturnUserByIdAsJson() throws Exception {
        mockMvc.perform(get("/users/{id}", EXPECTED_USER1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.id", is((int)returnedUser1.getId())))
                .andExpect(jsonPath("$.email", equalTo(returnedUser1.getEmail())))
                .andExpect(jsonPath("$.name", equalTo(returnedUser1.getName())));
    }

    @Test
    public void shouldReturnTwoUsersByNameAsJson() throws Exception {
        mockMvc.perform(get("/users")
                .param("name",EXPECTED_USERS_NAME)
                .param("pageSize","2")
                .param("pageNum", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is((int)returnedUser1.getId())))
                .andExpect(jsonPath("$[0].email", equalTo(returnedUser1.getEmail())))
                .andExpect(jsonPath("$[0].name", equalTo(returnedUser1.getName())))
                .andExpect(jsonPath("$[1].id", is((int)returnedUser2.getId())))
                .andExpect(jsonPath("$[1].email", equalTo(returnedUser2.getEmail())))
                .andExpect(jsonPath("$[1].name", equalTo(returnedUser2.getName())));
    }

    @Test
    public void shouldCreateUserAndReturnUserWithSettedIdAsJson() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(convertObjectToJson(returnedUser1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.id", is((int)returnedUser2.getId())))
                .andExpect(jsonPath("$.email", equalTo(returnedUser2.getEmail())))
                .andExpect(jsonPath("$.name", equalTo(returnedUser2.getName())));
    }

    @Test
    public void shouldUpdateUserAndReturnUpdatedUserAsJson() throws Exception {
        mockMvc.perform(put("/users")
                .contentType(APPLICATION_JSON_CHARSET_UTF_8)
                .content(convertObjectToJson(returnedUser1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(jsonPath("$.id", is((int)returnedUser2.getId())))
                .andExpect(jsonPath("$.email", equalTo(returnedUser2.getEmail())))
                .andExpect(jsonPath("$.name", equalTo(returnedUser2.getName())));
    }


    @Test
    public void shouldRemoveUserAndReturnTrue() throws Exception {
        mockMvc.perform(delete("/users/{id}", returnedUser1.getId())
                .contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(status().isOk()).andExpect(content().string("true"));
    }

    @Test
    public void shouldNotRemoveUserAndReturnFalse() throws Exception {
        mockMvc.perform(delete("/users/{id}", 12312312312L)
                .contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(status().isOk()).andExpect(content().string("false"));
    }


    @Test
    @Ignore
    public void shouldBookTicketAndReturnItAsJson() throws Exception {
        mockMvc.perform(post("/tickets")
                .param("userId", String.valueOf(EXPECTED_USER1_ID))
                .param("eventId", String.valueOf(EXPECTED_EVENT1_ID))
                .param("place", String.valueOf(EXPECTED_TICKETS_PLACE))
                .param("category", EXPECTED_TICKETS_CATEGORY.toString())
                .contentType(APPLICATION_JSON_CHARSET_UTF_8))
                    .andExpect(status().isOk())
                    .andExpect(content().string(convertObjectToJson(ticket1)));
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
        return mapper.writeValueAsString(object);
    }
}