package com.epam.cdp.sanin.controllers.exception;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

@Component
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger LOG = Logger.getLogger(CustomHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        LOG.info(MessageFormat.format("an exception occurs during execution of [{0}] with root cause [{1}]", handler.getClass().getSimpleName(), ex.getMessage()));
        ModelAndView modelAndView = new ModelAndView("exceptionPage");
        modelAndView.addObject("reason", ex.getMessage());
        return modelAndView;
    }
}