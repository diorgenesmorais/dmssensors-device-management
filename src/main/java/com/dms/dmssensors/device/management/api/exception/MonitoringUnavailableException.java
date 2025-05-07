package com.dms.dmssensors.device.management.api.exception;

public class MonitoringUnavailableException extends RuntimeException {
    public MonitoringUnavailableException(String message) {
        super(message);
    }

    public MonitoringUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public MonitoringUnavailableException(Throwable cause) {
        super(cause);
    }
}
