package com.task.searcherservice.controller.exceptionhandler.errorresponse;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Alexander Shakhov
 */
@Component
@AllArgsConstructor
public class ErrorResponseFactory {

    @NonNull
    public ErrorResponse create(@NonNull HttpStatus status, @NonNull ErrorCodes errorCode, @NonNull String message) {
        return new ErrorResponse(status.value(), errorCode.getMessage(), message);
    }

    @NonNull
    public ErrorResponse create(@NonNull ErrorCodes errorCode, @NonNull String message) {
        return create(errorCode.getStatus(), errorCode, message);
    }
}
