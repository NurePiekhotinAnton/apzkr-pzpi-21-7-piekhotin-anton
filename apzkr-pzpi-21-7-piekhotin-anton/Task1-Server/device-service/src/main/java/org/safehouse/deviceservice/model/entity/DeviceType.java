package org.safehouse.deviceservice.model.entity;

public enum DeviceType {
	LIGHT,
	FAN,
	AC,
	TV,
	DOOR,
	WINDOW,
	CAMERA,
	ALARM,
	SENSOR,
	OTHER;

	public static boolean contains(String type) {
		for (DeviceType deviceType : DeviceType.values()) {
			if (deviceType.name().equals(type)) {
				return true;
			}
		}
		return false;
	}
}
