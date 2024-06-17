package org.safehouse.authservice.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RequestMetaInfo implements Serializable {

	@Serial
	private final static long serialVersionUID = 5449687943362516710L;

	@NonNull
	private String correlationId;
	private String currentClientIpAddress;
	private Long currentClientGuestId;
}

