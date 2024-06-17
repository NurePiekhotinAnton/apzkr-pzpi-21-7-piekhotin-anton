package org.safehouse.authservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequestDto {

	@Size(max = 50)
	@Email(regexp = ".+@.+\\..+", message = "Email should be valid")
	@NotBlank(message = "Email should be valid and not blank")
	String email;

	@Size(max = 500)
	@NotBlank(message = "Password should be valid and not blank")
	String password;
}
