package com.epam.cdp.sanin.controllers.converters;

import com.google.common.collect.Lists;
import model.interfaces.Event;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * converts Event to list of string, where each string is an actual value form each property of the Event
 */
@Component
public class EventConverter implements Converter<Event,List<String>>  {
    @Override
    public List<String> convert(Event event) {
        if(Objects.isNull(event)){
            return Lists.newArrayList();
        }
        //@formatter:off
        return Lists.newArrayList(
                String.valueOf(event.getId()),
                event.getTitle(),
                event.getDate().toString(),
                event.getTicketPrice().toString()
        );
        //@formatter:on
    }
}
