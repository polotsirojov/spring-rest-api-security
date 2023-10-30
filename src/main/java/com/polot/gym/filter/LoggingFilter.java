package com.polot.gym.filter;

import com.polot.gym.config.RequestContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String transactionId = generateTransactionId();
        RequestContextHolder.setRequest(request);
        request.setAttribute("TransactionId", transactionId);
        logger.info("Transaction started. TransactionId: {}, Endpoint: {}", transactionId, request.getRequestURI());
        filterChain.doFilter(request, response);
        logger.info("Transaction completed. TransactionId: {}, Response status: {}", transactionId, response.getStatus());
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

}
