package com.dms.dmssensors.device.management.api.client.impl;

import com.dms.dmssensors.device.management.api.client.SensorMonitoringClient;
import com.dms.dmssensors.device.management.api.exception.MonitoringUnavailableException;
import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Slf4j
@Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient restClient;

    public SensorMonitoringClientImpl(
            RestClient.Builder builder,
            @Value("${sensor.monitoring.base.url}") String baseUrl
    ) {
        this.restClient = builder
                .requestFactory(generateClientHttpRequestFactory())
                .baseUrl(baseUrl)
                .defaultStatusHandler(HttpStatusCode::isError, (req, resp) -> {
                    log.error("Erro ao chamar serviço de monitoramento");
                    throw new SensorMonitoringClientBadGatewayException("Erro ao chamar serviço de monitoramento");
                })
                .build();
    }

    private ClientHttpRequestFactory generateClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(5));
        factory.setConnectTimeout(Duration.ofSeconds(3));

        return factory;
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
}
