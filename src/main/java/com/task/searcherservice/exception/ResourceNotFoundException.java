package com.task.searcherservice.exception;

import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author Alexander Shakhov
 */
@Getter
@AllArgsConstructor
public class ResourceNotFoundException extends AbstractFormattedException {
    private static final long serialVersionUID = 1L;
    @NonNull
    private final ErrorResponse errorResponse;
}
