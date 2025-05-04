package com.dms.dmssensors.device.management.domain.repository;

import com.dms.dmssensors.device.management.domain.model.Sensor;
import com.dms.dmssensors.device.management.domain.model.SensorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SensorRepository extends JpaRepository<Sensor, SensorId> {
    @Modifying
    @Query(value = "UPDATE sensor SET enabled = true WHERE id = :id", nativeQuery = true)
    void enableSensor(@Param("id") Long id);
}
