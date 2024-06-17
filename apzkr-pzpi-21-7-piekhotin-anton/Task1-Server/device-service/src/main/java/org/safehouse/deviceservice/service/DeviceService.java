package org.safehouse.deviceservice.service;

import lombok.RequiredArgsConstructor;
import org.safehouse.deviceservice.model.dto.DeviceInfoDto;
import org.safehouse.deviceservice.model.dto.TemperatureAndHumidityData;
import org.safehouse.deviceservice.model.entity.Device;
import org.safehouse.deviceservice.model.entity.TemperatureAndHumidity;
import org.safehouse.deviceservice.repository.DeviceRepository;
import org.safehouse.deviceservice.repository.TempHumDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing devices.
 */
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final TempHumDataRepository tempHumDataRepository;

    /**
     * Fetches a device by its ID.
     *
     * @param id The ID of the device.
     * @return The device, or null if no device with the given ID exists.
     */
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    /**
     * Checks if a device with the given ID exists.
     *
     * @param id The ID of the device.
     * @return True if a device with the given ID exists, false otherwise.
     */
    public boolean existsById(Long id) {
        if (id == null || id <= 0)
            return false;
        return deviceRepository.existsById(id);
    }

    /**
     * Fetches all devices associated with a given house.
     *
     * @param houseId The ID of the house.
     * @return A list of devices associated with the house, or null if the house ID is invalid.
     */
    public List<Device> getDevicesByHouseId(Long houseId) {
        if (houseId == null || houseId <= 0)
            return null;
        return deviceRepository.findAllByHouseId(houseId);
    }

    /**
     * Creates a new device.
     *
     * @param device The device to create.
     * @return The created device, or null if a device with the same ID already exists.
     */
    public Device createDevice(Device device) {
        if (!existsById(device.getId())) {
            return deviceRepository.save(device);
        }
        return null;
    }

    /**
     * Deletes a device by its ID.
     *
     * @param id The ID of the device.
     * @return True if the device was successfully deleted, false otherwise.
     */
    public boolean deleteDeviceById(Long id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Device> getAllDevices() {
        if (deviceRepository.findAll().isEmpty())
            return null;
        return deviceRepository.findAll();
    }

    public boolean editDevice(Long id, DeviceInfoDto deviceInfoDto) {
        if (existsById(id)) {
            Device deviceToEdit = deviceRepository.findById(id).orElse(null);
            if (deviceToEdit != null) {
                deviceToEdit.setType(deviceInfoDto.getType());
                deviceRepository.save(deviceToEdit);
                return true;
            }
        }
        return false;
    }

//    TODO: Implement call to device
//	public DeviceInfoDto getDeviceInfo(Long deviceId) {
//		if (existsById(deviceId)) {
//		}
//	}

    public boolean saveDataTempHum(Long device_id, TemperatureAndHumidityData data) {
        if (existsById(device_id)) {
			TemperatureAndHumidity tempHumToSave = TemperatureAndHumidity.builder()
					.deviceId(device_id)
					.temperature(data.getTemperature())
					.humidity(data.getHumidity())
					.timestamp(LocalDateTime.now())
					.build();
			tempHumDataRepository.save(tempHumToSave);
            return true;
		}

        return false;
    }

}