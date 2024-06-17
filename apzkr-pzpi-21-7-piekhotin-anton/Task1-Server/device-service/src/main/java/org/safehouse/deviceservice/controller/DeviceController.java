package org.safehouse.deviceservice.controller;

import lombok.RequiredArgsConstructor;
import org.safehouse.deviceservice.model.dto.DeviceInfoDto;
import org.safehouse.deviceservice.model.dto.TemperatureAndHumidityData;
import org.safehouse.deviceservice.model.entity.Device;
import org.safehouse.deviceservice.model.entity.DeviceType;
import org.safehouse.deviceservice.model.entity.TemperatureAndHumidity;
import org.safehouse.deviceservice.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {

	private final DeviceService deviceService;

	@GetMapping("/{id}")
	public ResponseEntity<?> getDeviceById(@PathVariable Long id) {
		if (id == null || id <= 0)
			return ResponseEntity.badRequest().body("Invalid id");
		if (!deviceService.existsById(id)) {
			return ResponseEntity.badRequest().body("Device with this id not found");
		}
		return ResponseEntity.ok(deviceService.getDeviceById(id));
	}

	@PostMapping("/{houseId}/create")
	public ResponseEntity<?> createDevice(@PathVariable Long houseId, @RequestBody DeviceInfoDto deviceInfoDto) {
		if (houseId == null || houseId <= 0)
			return ResponseEntity.badRequest().body("Invalid house id");

		if (deviceInfoDto.getType() == null || !DeviceType.contains(deviceInfoDto.getType().name()))
			return ResponseEntity.badRequest().body("Invalid device type");

		Device deviceToCreate = Device.builder()
				.houseId(houseId)
				.type(deviceInfoDto.getType())
				.build();
		deviceService.createDevice(deviceToCreate);

		return ResponseEntity.status(HttpStatus.CREATED).body(deviceToCreate);
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteDeviceById(@PathVariable Long id) {
		if (id == null || id <= 0)
			return ResponseEntity.badRequest().body("Invalid id");
		if (!deviceService.existsById(id)) {
			return ResponseEntity.badRequest().body("Device with this id not found");
		}
		return ResponseEntity.ok(deviceService.deleteDeviceById(id));
	}

	@GetMapping("/my/{houseId}")
	public ResponseEntity<?> getDevicesByHouseId(@PathVariable Long houseId) {
		if (houseId == null || houseId <= 0)
			return ResponseEntity.badRequest().body("Invalid house id");

		return ResponseEntity.ok(deviceService.getDevicesByHouseId(houseId));
	}

	@GetMapping("/{deviceId}/info")
	public ResponseEntity<?> getDeviceInfo(@PathVariable Long deviceId) {
		if (deviceId == null || deviceId <= 0)
			return ResponseEntity.badRequest().body("Invalid id");

		return null;
//		return ResponseEntity.ok(deviceService.getDeviceInfo(deviceId));
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllDevices() {
		List<Device> devicesToReturn = deviceService.getAllDevices();
		if (devicesToReturn == null || devicesToReturn.isEmpty())
			return ResponseEntity.ok().body(List.of());
		return ResponseEntity.ok(devicesToReturn);
	}

	@PostMapping("/edit/{id}")
	public ResponseEntity<?> editDevice(@PathVariable Long id, @RequestBody DeviceInfoDto deviceInfoDto) {
		if (id == null || id <= 0)
			return ResponseEntity.badRequest().body("Invalid id");
		if (deviceInfoDto == null) {
			return ResponseEntity.badRequest().body("Invalid data");
		}
		if (deviceService.editDevice(id, deviceInfoDto)) {
			return ResponseEntity.ok().body("Device edited successfully");
		}
		return ResponseEntity.badRequest().body("Error while editing device");
	}

	@PostMapping("/{device_id}/save-data/temp-hum")
	public ResponseEntity<?> saveData(@PathVariable Long device_id, @RequestBody TemperatureAndHumidityData data) {
		if (device_id == null || device_id <= 0 || !deviceService.existsById(device_id))
			return ResponseEntity.badRequest().body("Invalid id");
		if (data == null)
			return ResponseEntity.badRequest().body("Invalid data");
		if (deviceService.saveDataTempHum(device_id, data)) {
			return ResponseEntity.ok().body("Data saved successfully");
		}
		return ResponseEntity.badRequest().body("Error while saving data");
	}

}
