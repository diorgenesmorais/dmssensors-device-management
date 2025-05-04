package com.dms.dmssensors.device.management.api.model;

import com.dms.dmssensors.device.management.domain.model.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    Sensor toEntity(SensorInput input);

    default SensorOutput toOutput(Sensor sensor) {
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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    void updateSensorFromInput(SensorInput input, @MappingTarget Sensor sensor);
}
