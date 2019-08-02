package com.task.searcherservice.controller.error;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {
    BAD_REQUEST_FORMAT(HttpStatus.BAD_REQUEST, "100"),
    RESOURCE_IS_NOT_FOUND(HttpStatus.NOT_FOUND, "101"),
    API_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "102");

    private static final String SERVICE_CODE = "Searcher";
    private final HttpStatus status;
    private final String code;

    ErrorCodes(@NonNull HttpStatus status, @NonNull String code) {
        this.status = status;
        this.code = String.format("%s.%s", ErrorCodes.SERVICE_CODE, code);
    }

}
