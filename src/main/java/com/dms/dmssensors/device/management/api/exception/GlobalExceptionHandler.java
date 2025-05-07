package com.dms.dmssensors.device.management.api.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail body = createProblemDetail(ex, status, ex.getMessage(), null, null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(MonitoringUnavailableException.class)
    public ResponseEntity<Object> handleMonitoringUnavailable(MonitoringUnavailableException ex, WebRequest request) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        ProblemDetail body = createProblemDetail(ex, status, ex.getMessage(), null, null, request);
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }
}
