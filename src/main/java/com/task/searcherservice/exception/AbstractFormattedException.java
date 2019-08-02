package com.task.searcherservice.exception;

import lombok.NonNull;

/**
 * Base form of runtime exception, adds convenient formatting.
 */
public abstract class AbstractFormattedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AbstractFormattedException() {
        super();
    }

    public AbstractFormattedException(@NonNull String message, Object... params) {
        super(String.format(message, params));
    }

    public AbstractFormattedException(@NonNull Throwable cause, @NonNull String message, Object... params) {
        super(String.format(message, params), cause);
    }

    public AbstractFormattedException(@NonNull Throwable cause) {
        super(cause);
    }
}
