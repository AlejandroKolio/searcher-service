package com.task.searcherservice.exception;

import lombok.NonNull;

public class ResourceNotFoundException extends AbstractFormattedException {
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Resource not found. resourceType='%s', id='%s'";

    public ResourceNotFoundException(@NonNull final String resourceType, @NonNull final String id) {
        super(MESSAGE, resourceType, id);
    }

    public ResourceNotFoundException(@NonNull final Throwable cause, @NonNull final String resourceType,
        @NonNull final String id) {
        super(MESSAGE, resourceType, id);
    }
}
