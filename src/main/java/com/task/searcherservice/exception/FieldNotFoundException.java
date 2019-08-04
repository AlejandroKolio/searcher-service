package com.task.searcherservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author Alexander Shakhov
 */
@Getter
@AllArgsConstructor
public class FieldNotFoundException extends AbstractFormattedException {
    private static final long serialVersionUID = 1L;
    @NonNull
    private final String message;
}
