package com.task.searcherservice.controller.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorCodes;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponse;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponseFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Alexander Shakhov
 */
@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class DefaultResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @NonNull
    private final ErrorResponseFactory errorResponseFactory;

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException exception,
            @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        final Throwable cause = exception.getCause();
        if (cause instanceof MismatchedInputException) {
            final MismatchedInputException mismatchedInputException = (MismatchedInputException) cause;
            final List<JsonMappingException.Reference> fieldsPath = mismatchedInputException.getPath();
            final String message = String.format("Value in field '%s' is invalid",
                    fieldsPath.get(fieldsPath.size() - 1).getFieldName());
            return createResponse(exception, status, message);
        }

        return super.handleHttpMessageNotReadable(exception, headers, status, request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        final String message = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return createResponse(exception, status, message);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception exception, @Nullable Object body,
            @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return createResponse(exception, status, status.getReasonPhrase());
    }

    @NonNull
    private ResponseEntity<Object> createResponse(@NonNull Exception exception, @NonNull HttpStatus status,
            String message) {
        final ErrorResponse response = errorResponseFactory.create(status, ErrorCodes.WRONG_REQUEST_FORMAT, message);
        logger.error(String.format("%s", response), exception);
        return new ResponseEntity<>(response, status);
    }
}
