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

    @Transactional
    public SensorOutput updateSensor(SensorInput input, TSID sensorId) {
        Sensor fetchSensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new EntityNotFoundException("Sensor not found"));

        Sensor sensor = sensorMapper.toEntity(input);
        sensor.setId(new SensorId(sensorId));
        sensor.setEnabled(fetchSensor.getEnabled());

        sensor = sensorRepository.saveAndFlush(sensor);

        return sensorMapper.toOutput(sensor);
    }
}
