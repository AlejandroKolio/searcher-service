package com.task.searcherservice.controller.error;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ErrorResponseFactory {

    @NonNull
    public ErrorResponse create(@NonNull HttpStatus status, @NonNull ErrorCodes errorCode, @NonNull String message) {
        return new ErrorResponse(status.value(), errorCode.getCode(), message);
    }
}
