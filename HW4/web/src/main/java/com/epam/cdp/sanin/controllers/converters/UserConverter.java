package com.epam.cdp.sanin.controllers.converters;

import com.google.common.collect.Lists;
import model.interfaces.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


/**
 * converts User to list of string, where each string is an actual value form each property of the User
 */
@Component
public class UserConverter implements Converter<User,List<String>> {
    @Override
    public List<String> convert(User user) {
        if(Objects.isNull(user)){
            return Lists.newArrayList();
        }
        return Lists.newArrayList(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getName()
        );
    }
}
