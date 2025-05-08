package com.dms.dmssensors.device.management.api.client;

import com.dms.dmssensors.device.management.api.client.impl.SensorMonitoringClientBadGatewayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
@Slf4j
@RequiredArgsConstructor
public class RestClientFactory {

    @Value("${sensor.monitoring.base.url}")
    private String sensorMonitoringBaseUrl;
    private final RestClient.Builder builder;

    public RestClient temperatureMonitoringClient() {
        return builder
                .requestFactory(generateClientHttpRequestFactory())
                .baseUrl(sensorMonitoringBaseUrl)
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
}
