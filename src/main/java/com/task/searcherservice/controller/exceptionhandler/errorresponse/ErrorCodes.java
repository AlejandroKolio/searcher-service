package com.task.searcherservice.controller.exceptionhandler.errorresponse;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

/**
 * @author Alexander Shakhov
 */
@Getter
public enum ErrorCodes {
    WRONG_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "Wrong format input."),
    RESOURCE_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "Resourse is unknown or not found."),
    FIELD_IS_NOT_FOUND(HttpStatus.NO_CONTENT, "Field is changed or not found."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Api client error");

    private final HttpStatus status;
    private final String message;

    ErrorCodes(@NonNull HttpStatus status, @NonNull String message) {
        this.status = status;
        this.message = String.format("%s - %s", status, message);
    }

    @NonNull
    public ErrorCodes of(@NonNull String code) {
        return Arrays.stream(ErrorCodes.values())
                .filter(errorCodes -> errorCodes.status.equals(HttpStatus.valueOf(code)))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("There is no such Error"));
    }
}
