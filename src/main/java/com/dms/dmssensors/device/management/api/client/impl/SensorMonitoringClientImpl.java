package com.dms.dmssensors.device.management.api.client.impl;

import com.dms.dmssensors.device.management.api.client.RestClientFactory;
import com.dms.dmssensors.device.management.api.client.SensorMonitoringClient;
import com.dms.dmssensors.device.management.api.exception.MonitoringUnavailableException;
import com.dms.dmssensors.device.management.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

/**
 * @deprecated This class is deprecated and should not be used in new code.
 * It is kept for backward compatibility and may be removed in future versions.
 *
 * @since 1.0.0
 */
@Slf4j
//@Component
@SuppressWarnings("java:S1133")
@Deprecated(since = "1.0.0")
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient restClient;

    public SensorMonitoringClientImpl(RestClientFactory factory) {
        this.restClient = factory.temperatureMonitoringClient();
    }

    public void fallbackEnableMonitoring(TSID sensorId, Throwable t) {
        log.error("Fallback: não foi possível habilitar monitoramento para sensor {}", sensorId, t);
        throw new MonitoringUnavailableException("Serviço de monitoramento indisponível no momento");
    }

    public void fallbackDisableMonitoring(TSID sensorId, Throwable t) {
        log.error("Fallback: não foi possível desabilitar monitoramento para sensor {}", sensorId, t);
        throw new MonitoringUnavailableException("Serviço de monitoramento indisponível no momento");
    }

    //@CircuitBreaker(name = "sensorMonitoring", fallbackMethod = "fallbackEnableMonitoring")
    @Override
    public void enableMonitoring(TSID sensorId) {
        this.restClient.put()
                .uri("/api/sensors/{sensorId}/monitoring/enable", sensorId)
                .retrieve()
                .toBodilessEntity();
    }

    //@CircuitBreaker(name = "sensorMonitoring", fallbackMethod = "fallbackDisableMonitoring")
    @Override
    public void disableMonitoring(TSID sensorId) {
        this.restClient.delete()
                .uri("/api/sensors/{sensorId}/monitoring/enable", sensorId)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public SensorMonitoringOutput getMonitoring(TSID sensorId) {
        return this.restClient.get()
                .uri("/api/sensors/{sensorId}/monitoring", sensorId)
                .retrieve()
                .body(SensorMonitoringOutput.class);
    }
}
