package org.safehouse.usermicroservice.service;

import lombok.RequiredArgsConstructor;
import org.safehouse.usermicroservice.model.dto.GuestInfoDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserService {
	private final RestTemplate restTemplate;

	public GuestInfoDto editUser(Long id, GuestInfoDto guestInfoDto) {
		String url = "http://localhost:8181/api/v1/auth/edit-user/" + id;

		HttpEntity<GuestInfoDto> requestUpdate = new HttpEntity<>(guestInfoDto);

		ResponseEntity<GuestInfoDto> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				requestUpdate,
				GuestInfoDto.class
		);

		return response.getBody();
	}

	public GuestInfoDto getUserById(Long id) {
		String url = "http://localhost:8181/api/v1/auth/user/" + id;

		return restTemplate.getForObject(url, GuestInfoDto.class);
	}

}
