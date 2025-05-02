package com.dms.dmssensors.device.management.domain.repository;

import com.dms.dmssensors.device.management.domain.model.Sensor;
import com.dms.dmssensors.device.management.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
}
