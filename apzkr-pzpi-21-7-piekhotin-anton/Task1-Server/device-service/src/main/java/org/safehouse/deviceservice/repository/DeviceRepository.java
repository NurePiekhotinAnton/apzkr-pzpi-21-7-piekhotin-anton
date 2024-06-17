package org.safehouse.deviceservice.repository;

import org.safehouse.deviceservice.model.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
	List<Device> findAllByHouseId(Long houseId);
}
