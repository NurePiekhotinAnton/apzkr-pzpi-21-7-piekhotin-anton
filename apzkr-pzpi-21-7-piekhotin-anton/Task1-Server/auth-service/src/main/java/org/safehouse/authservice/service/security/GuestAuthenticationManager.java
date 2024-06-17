package org.safehouse.authservice.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.safehouse.authservice.model.dto.AuthRequestDto;
import org.safehouse.authservice.model.dto.JwtResponseDto;
import org.safehouse.authservice.service.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class GuestAuthenticationManager {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	public Authentication authenticateRequest(AuthRequestDto requestDto) {
		return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword()));
	}

	public JwtResponseDto handlingSuccessfulAuthentication(Authentication auth) {
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		UsernamePasswordAuthenticationToken authToken =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);

		String jwt = jwtUtil.generateToken(userDetails);
		String role = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).findFirst().orElse("");

		return JwtResponseDto.builder()
				.token(jwt)
				.expiresAt(convertToLocalDateViaInstant(jwtUtil.extractExpiration(jwt)))
				.type("Bearer")
				.guestId(userDetails.getId())
				.email(userDetails.getUsername())
				.role(role)
				.build();
	}

	private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}

}
