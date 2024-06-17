package org.safehouse.deviceservice.repository;

import org.safehouse.deviceservice.model.entity.TemperatureAndHumidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempHumDataRepository extends JpaRepository<TemperatureAndHumidity, Long> {

}
