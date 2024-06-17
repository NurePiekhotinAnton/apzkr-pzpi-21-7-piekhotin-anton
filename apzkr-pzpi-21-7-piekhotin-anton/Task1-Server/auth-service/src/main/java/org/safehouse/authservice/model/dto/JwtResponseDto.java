package org.safehouse.authservice.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record JwtResponseDto(String token,
							 LocalDate expiresAt,
							 String type,
							 Long guestId,
							 String email,
							 String role) {
}
