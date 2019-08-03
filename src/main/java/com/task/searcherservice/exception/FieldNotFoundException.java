package com.task.searcherservice.exception;

import lombok.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author Alexander Shakhov
 */
@ControllerAdvice
public class FieldNotFoundException extends AbstractFormattedException {
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Field not found. resourceType='%s', id='%s'";

    public FieldNotFoundException() {
        super();
    }

    public FieldNotFoundException(@NonNull final String resourceType, @NonNull final String id) {
        super(MESSAGE, resourceType, id);
    }

    public FieldNotFoundException(@NonNull final Throwable cause, @NonNull final String resourceType,
        @NonNull final String id) {
        super(MESSAGE, resourceType, id);
    }
}
