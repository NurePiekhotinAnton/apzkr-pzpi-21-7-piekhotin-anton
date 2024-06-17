package org.safehouse.deviceservice.model.dto;

import lombok.Data;
import org.safehouse.deviceservice.model.entity.DeviceType;

@Data
public class DeviceInfoDto {
	DeviceType type;

	public DeviceInfoDto(String type) {
		if (DeviceType.contains(type)) {
			this.type = DeviceType.valueOf(type);
		} else {
			this.type = DeviceType.OTHER;
		}
	}
}
