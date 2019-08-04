package com.task.searcherservice.controller.exceptionhandler;

import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponse;
import com.task.searcherservice.exception.FieldNotFoundException;
import com.task.searcherservice.exception.RemoteServiceException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Alexander Shakhov
 */
@Slf4j
@ControllerAdvice
public class FieldNoFoundExceptionHandler {
    @ExceptionHandler(FieldNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleException(@NonNull RemoteServiceException exception) {
        final ErrorResponse errorResponse = exception.getErrorResponse();
        log.error(String.format("%s", errorResponse));
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }
}
