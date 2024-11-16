package com.epam.xstack.gym.trainer.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Order(1)
@Component
public class LoggingFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestedURI = httpRequest.getRequestURI();

        logger.info("Request received for URI: {}, method: {}", requestedURI, httpRequest.getMethod());
        logger.debug("Request came from: {}", httpRequest.getRemoteAddr());

        try {
            chain.doFilter(request, response);
        } finally {
            logger.info("Transaction completed");
        }
    }
}
