package com.task.searcherservice.controller.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorCodes;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponse;
import com.task.searcherservice.controller.exceptionhandler.errorresponse.ErrorResponseFactory;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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
    public ModelAndView resolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            Object handler, @NonNull Exception exception) {
        final ErrorCodes code = ErrorCodes.SERVER_ERROR;

        final ErrorResponse errorResponse = errorResponseFactory.create(code, "Server Error");
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
