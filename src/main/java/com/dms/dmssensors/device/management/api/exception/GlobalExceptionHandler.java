package com.dms.dmssensors.device.management.api.exception;

import com.dms.dmssensors.device.management.api.client.impl.SensorMonitoringClientBadGatewayException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;

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

    @ExceptionHandler(SensorMonitoringClientBadGatewayException.class)
    public ResponseEntity<Object> handleMonitoringBadGateway(SensorMonitoringClientBadGatewayException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        ProblemDetail body = ProblemDetail.forStatus(status);
        body.setTitle("Bad Gateway");
        body.setDetail(ex.getMessage());
        body.setType(URI.create("/api/errors/bad-gateway"));

        return super.handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({
            SocketTimeoutException.class,
            ConnectException.class,
            ClosedChannelException.class
    })
    public ResponseEntity<Object> handleBadGateway(IOException ex, WebRequest request) {
        HttpStatus status = HttpStatus.GATEWAY_TIMEOUT;
        ProblemDetail body = ProblemDetail.forStatus(status);
        body.setTitle("Gateway timeout");
        body.setDetail(ex.getMessage());
        body.setType(URI.create("/api/errors/gateway-timeout"));

        return super.handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }
}
