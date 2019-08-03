package com.task.searcherservice.controller.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Alexander Shakhov
 */
@Slf4j
@Component
@AllArgsConstructor
public class DefaultExceptionResolver implements HandlerExceptionResolver {

    @NonNull
    private final ObjectMapper mapper;
    @NonNull
    private final ErrorResponseFactory errorResponseFactory;

    @Override
    public ModelAndView resolveException(@NonNull final HttpServletRequest request,
        @NonNull final HttpServletResponse response, @Nullable final Object handler,
        @NonNull final Exception exception) {

        final ErrorCodes code = ErrorCodes.API_CLIENT_ERROR;

        final ErrorResponse errorResponse = errorResponseFactory.create(code.getStatus(), code, "Server Error");
        log.error("{}", errorResponse, exception);
        response.setStatus(code.getStatus().value());
        try (final OutputStream outputStream = response.getOutputStream()) {
            mapper.writeValue(outputStream, errorResponse);
        } catch (IOException e) {
            log.error("exception while writing response", e);
        }
        return new ModelAndView();
    }
}
