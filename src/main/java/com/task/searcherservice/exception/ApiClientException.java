package com.task.searcherservice.exception;

import lombok.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Alexander Shakhov
 */
@ControllerAdvice
public class ApiClientException extends AbstractFormattedException {
    private static final long serialVersionUID = 1L;

    public ApiClientException() {
        super();
    }

    public ApiClientException(@NonNull String message, Object... params) {
        super(message, params);
    }

    public ApiClientException(@NonNull Throwable cause, @NonNull String message, Object... params) {
        super(cause, message, params);
    }

    public ApiClientException(@NonNull Throwable cause) {
        super(cause);
    }
}
