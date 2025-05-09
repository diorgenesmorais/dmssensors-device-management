package com.dms.dmssensors.device.management.api.client;

import com.dms.dmssensors.device.management.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;

public interface SensorMonitoringClient {
    void enableMonitoring(TSID sensorId);
    void disableMonitoring(TSID sensorId);
    SensorMonitoringOutput getMonitoring(TSID sensorId);
}
