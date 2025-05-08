package com.dms.dmssensors.device.management.api.client.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class SensorMonitoringClientBadGatewayException extends RuntimeException {
    public SensorMonitoringClientBadGatewayException(String message) {
        super(message);
    }
}
