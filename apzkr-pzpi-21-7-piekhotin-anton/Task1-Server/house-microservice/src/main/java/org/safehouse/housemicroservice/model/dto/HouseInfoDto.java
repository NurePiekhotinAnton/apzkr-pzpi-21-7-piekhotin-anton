package org.safehouse.housemicroservice.model.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class HouseInfoDto {

	String name;

	String address;
}
