package com.task.searcherservice.controller.exceptionhandler;

import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorCodes;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponse;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponseFactory;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

/**
 * @author Alexander Shakhov
 */
@Slf4j
public abstract class AbstractWebExceptionHandler {
    @Autowired
    @NonNull
    private ErrorResponseFactory errorResponseFactory;

    protected ResponseEntity<ErrorResponse> prepareResponseEntity(@NonNull String message,
            @NonNull ErrorCodes errorCode, @NonNull Exception exception) {
        final ErrorResponse response = errorResponseFactory.create(errorCode, message);
        log.error(String.format("%s", response), exception);
        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}
