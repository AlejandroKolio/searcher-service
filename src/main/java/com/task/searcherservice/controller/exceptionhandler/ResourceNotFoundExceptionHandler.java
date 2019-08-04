package com.task.searcherservice.controller.exceptionhandler;

import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorCodes;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponse;
import com.task.searcherservice.exception.ResourceNotFoundException;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Alexander Shakhov
 */
@ControllerAdvice
public class ResourceNotFoundExceptionHandler extends AbstractWebExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleException(@NonNull ResourceNotFoundException exception) {
        return prepareResponseEntity(exception.getMessage(), ErrorCodes.RESOURCE_IS_NOT_FOUND, exception);
    }
}
