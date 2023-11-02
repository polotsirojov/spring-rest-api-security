package com.polot.gym.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
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
        log.error("GlobalExceptionHandler responseStatusException", e);
        return ProblemDetail.forStatusAndDetail(e.getStatusCode(), Optional.ofNullable(e.getReason()).orElse(""));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("GlobalExceptionHandler handleException", ex);
        ErrorResponse response = ErrorResponse.create(ex, HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
