package com.system.event_management.exception;

import com.system.event_management.model.ExceptionBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ExceptionBean> handleEventNotFoundException(EventNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionBean(ex.getMessage()),HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(InvalidEventIDException.class)
    public ResponseEntity<ExceptionBean> handleInvalidIDException(InvalidEventIDException ex) {
        return new ResponseEntity<>(new ExceptionBean(ex.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionBean> handleUserException(UserException ex) {
        return new ResponseEntity<>(new ExceptionBean(ex.getMessage()),ex.getHttpStatus());
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ExceptionBean> handleJwtException(JwtAuthenticationException ex) {
        return new ResponseEntity<>(new ExceptionBean(ex.getMessage()),ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBean> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new ExceptionBean(ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
