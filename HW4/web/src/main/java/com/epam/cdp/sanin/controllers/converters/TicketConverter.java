package com.epam.cdp.sanin.controllers.converters;

import com.google.common.collect.Lists;
import model.interfaces.Ticket;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * converts Ticket to list of string, where each string is an actual value form each property of the Ticket
 */
@Component
public class TicketConverter implements Converter<Ticket,List<String>> {

    @Override
    public List<String> convert(Ticket ticket) {
        if(Objects.isNull(ticket)){
            return Lists.newArrayList();
        }
        //@formatter:off
        return Lists.newArrayList(
                String.valueOf(ticket.getId()),
                String.valueOf(ticket.getEventId()),
                String.valueOf(ticket.getUserId()),
                ticket.getCategory().toString(),
                String.valueOf(ticket.getPlace())
        );
        //@formatter:on
    }
}
