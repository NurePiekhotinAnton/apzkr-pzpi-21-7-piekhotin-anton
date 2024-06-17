package org.safehouse.deviceservice.model.dto;

import lombok.Data;

@Data
public class TemperatureAndHumidityData {
	private Double temperature;
	private Double humidity;
}
