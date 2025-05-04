package com.dms.dmssensors.device.management.domain.service;

import com.dms.dmssensors.device.management.api.model.SensorInput;
import com.dms.dmssensors.device.management.api.model.SensorMapper;
import com.dms.dmssensors.device.management.api.model.SensorOutput;
import com.dms.dmssensors.device.management.domain.model.Sensor;
import com.dms.dmssensors.device.management.domain.model.SensorId;
import com.dms.dmssensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository sensorRepository;
    private final SensorMapper sensorMapper;

    public Sensor fetchById(TSID sensorId) {
        return sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found"));
    }

    @Transactional
    public SensorOutput updateSensor(SensorInput input, TSID sensorId) {
        Sensor fetchSensor = fetchById(sensorId);

        Sensor sensor = sensorMapper.toEntity(input);
        sensor.setId(new SensorId(sensorId));
        sensor.setEnabled(fetchSensor.getEnabled());

        sensor = sensorRepository.saveAndFlush(sensor);

        return sensorMapper.toOutput(sensor);
    }

    public void deleteSensor(TSID sensorId) {
        Sensor fetchSensor = fetchById(sensorId);

        sensorRepository.delete(fetchSensor);
    }

    @Transactional
    public void enableSensor(TSID sensorId) {
        fetchById(sensorId);

        sensorRepository.enableSensor(sensorId.toLong());
    }
}
