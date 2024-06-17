package org.safehouse.housemicroservice.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceDto {
	Long id;
	Long houseId;
	String type;
}
