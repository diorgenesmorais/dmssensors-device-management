package com.dms.dmssensors.device.management.domain.service;

import com.dms.dmssensors.device.management.api.client.SensorMonitoringClient;
import com.dms.dmssensors.device.management.api.model.SensorInput;
import com.dms.dmssensors.device.management.api.model.SensorOutput;
import com.dms.dmssensors.device.management.common.IdGenerate;
import com.dms.dmssensors.device.management.domain.model.Sensor;
import com.dms.dmssensors.device.management.domain.model.SensorId;
import com.dms.dmssensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final SensorMonitoringClient sensorMonitoringClient;

    public Sensor fetchById(TSID sensorId) {
        return sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found"));
    }

    private Sensor toEntity(SensorInput input) {
        return Sensor.builder()
                .name(input.getName())
                .ip(input.getIp())
                .location(input.getLocation())
                .protocol(input.getProtocol())
                .model(input.getModel())
                .build();
    }

    private SensorOutput toOutput(Sensor sensor) {
        if (sensor == null) return null;

        return SensorOutput.builder()
                .id(sensor.getId() != null ? sensor.getId().getValue() : null)
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .enabled(sensor.getEnabled())
                .build();
    }

    @Transactional
    public SensorOutput saveSensor(SensorInput input) {
        Sensor sensor = this.toEntity(input);
        sensor.setId(new SensorId(IdGenerate.generateTSID()));
        sensor.setEnabled(false);

        sensor = sensorRepository.saveAndFlush(sensor);

        return this.toOutput(sensor);
    }

    public SensorOutput getSensor(TSID sensorId) {
        Sensor fetchSensor = fetchById(sensorId);

        return this.toOutput(fetchSensor);
    }

    public Page<SensorOutput> searchSensors(Pageable pageable) {
        return sensorRepository.findAll(pageable)
                .map(this::toOutput);
    }

    @Transactional
    public SensorOutput updateSensor(SensorInput input, TSID sensorId) {
        Sensor fetchSensor = fetchById(sensorId);

        Sensor sensor = this.toEntity(input);
        sensor.setId(new SensorId(sensorId));
        sensor.setEnabled(fetchSensor.getEnabled());

        sensor = sensorRepository.saveAndFlush(sensor);

        return this.toOutput(sensor);
    }

    @Transactional
    public void deleteSensor(TSID sensorId) {
        Sensor fetchSensor = fetchById(sensorId);

        sensorRepository.delete(fetchSensor);

        sensorMonitoringClient.disableMonitoring(sensorId);
    }

    @Transactional
    public void enableSensor(TSID sensorId) {
        fetchById(sensorId);

        sensorRepository.enableOrDisableSensor(true, sensorId.toLong());

        sensorMonitoringClient.enableMonitoring(sensorId);
    }

    @Transactional
    public void disableSensor(TSID sensorId) {
        fetchById(sensorId);

        sensorRepository.enableOrDisableSensor(false, sensorId.toLong());

        sensorMonitoringClient.disableMonitoring(sensorId);
    }
}
