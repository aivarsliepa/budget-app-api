package com.aivarsliepa.budgetappapi.exceptionhandlers;

import com.aivarsliepa.budgetappapi.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class MvcExceptionHandlers {
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            DataIntegrityViolationException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadRequest(Exception ex) {
        log.debug(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus()
    public ResponseEntity handleGlobalException(Exception ex) {
        // for some reason, @ExceptionHandler does not handle correctly, if it is nested
        // this can be misleading if exception is wrapped
        for (var nested = ex.getCause(); nested != null; nested = nested.getCause()) {
            if (nested instanceof DataIntegrityViolationException) {
                log.debug(nested.getMessage());
                return ResponseEntity.badRequest().build();
            }
        }

        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void handleHttpRequestMethodNotSupportedException(Exception ex) {
        log.debug(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleResourceNotFoundException(Exception ex) {
        log.debug(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleBadCredentialsException(Exception ex) {
        log.debug(ex.getMessage());
    }
}
