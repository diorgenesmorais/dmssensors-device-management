package com.dms.dmssensors.device.management.api.controller;

import com.dms.dmssensors.device.management.api.model.SensorInput;
import com.dms.dmssensors.device.management.common.IdGenerate;
import com.dms.dmssensors.device.management.domain.model.Sensor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sensor createSensor(@RequestBody SensorInput sensor) {
        return Sensor.builder()
                .id(IdGenerate.generateTSID())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .location(sensor.getLocation())
                .protocol(sensor.getProtocol())
                .model(sensor.getModel())
                .enabled(false)
                .build();
    }
}
