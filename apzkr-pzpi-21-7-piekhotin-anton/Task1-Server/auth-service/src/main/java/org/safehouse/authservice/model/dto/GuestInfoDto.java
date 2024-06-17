package org.safehouse.authservice.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.safehouse.authservice.model.entity.GuestRole;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class GuestInfoDto {

	String email;

	String name;

	boolean isEnabled;

	GuestRole role;
}
