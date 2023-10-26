package com.polot.gym.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(ResponseStatusException.class)
    ProblemDetail handleException(ResponseStatusException e) {
        log.error("GlobalExceptionHandler handleException", e);
        return ProblemDetail.forStatusAndDetail(e.getStatusCode(), Optional.ofNullable(e.getReason()).orElse(""));
    }
}
