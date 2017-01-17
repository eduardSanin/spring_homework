package com.epam.cdp.sanin.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;
import static junit.framework.TestCase.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({
        "classpath:spring-core-app-context.xml",
        "classpath:excel-view-resolver-app-context.xml",
        "classpath:main-app-context.xml"
})
public class RestClientTest {
    /**
     * WEB APPLICATION SHOULD BE RUN ON 8081 port, it is client rather then tests
     */
    private static final String APPLICATION_PDF = "application/pdf";
    private static final String GET_TICKETS_FOR_USER = "http://localhost:8081/pdf/tickets/user/1?pageNum=0&pageSize=4";
    private static final String GET_TICKETS_FOR_EVENT = "http://localhost:8081/pdf/tickets/event/1?pageNum=0&pageSize=4";

    @Test
    public void shouldReturnTicketsForUserInPdf() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.valueOf(APPLICATION_PDF)));
        ResponseEntity<byte[]> response = restTemplate.exchange(GET_TICKETS_FOR_USER, HttpMethod.GET, new HttpEntity<byte[]>(headers), byte[].class);
        assertFalse(new String(response.getBody()).isEmpty());
    }

    @Test
    public void shouldReturnTicketsForEventInPdf() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.valueOf(APPLICATION_PDF)));
        ResponseEntity<byte[]> response = restTemplate.exchange(GET_TICKETS_FOR_EVENT, HttpMethod.GET, new HttpEntity<byte[]>(headers), byte[].class);
        assertFalse(new String(response.getBody()).isEmpty());
    }

}
