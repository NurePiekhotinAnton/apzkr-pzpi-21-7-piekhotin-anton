package org.safehouse.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.safehouse.authservice.model.dto.AuthRequestDto;
import org.safehouse.authservice.model.dto.GuestInfoDto;
import org.safehouse.authservice.model.dto.JwtResponseDto;
import org.safehouse.authservice.model.entity.Guest;
import org.safehouse.authservice.service.GuestService;
import org.safehouse.authservice.service.security.GuestAuthenticationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final GuestService guestService;
	private final GuestAuthenticationManager authenticationManagerFilter;

	@PostMapping("/sign-up")
	public ResponseEntity<?> registration(@RequestBody AuthRequestDto signUpRequestDto) {
		Guest createdGuest = guestService.createGuest(signUpRequestDto);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(createdGuest);
	}

	@PostMapping("/sign-in")
	public ResponseEntity<?> authenticate(@RequestBody AuthRequestDto authRequest) {
		Authentication auth = authenticationManagerFilter.authenticateRequest(authRequest);
		JwtResponseDto jwtResponse = authenticationManagerFilter.handlingSuccessfulAuthentication(auth);

		return ResponseEntity.ok(jwtResponse);
	}

	@GetMapping("/export-users")
	public ResponseEntity<?> exportUsers() {
		return ResponseEntity.ok().body(guestService.exportUsers());
	}

	@PostMapping("/edit-user/{id}")
	public ResponseEntity<?> editUser(@RequestBody GuestInfoDto guestInfoDto, @PathVariable Long id) {
		if (guestInfoDto == null) {
			return ResponseEntity.badRequest().body("Invalid data");
		}
		if (guestService.editUser(id, guestInfoDto)) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().body("Error while editing user");
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		if (id == null || id <= 0) {
			return ResponseEntity.badRequest().body("Invalid id");
		}
		if (!guestService.existsById(id)) {
			return ResponseEntity.badRequest().body("User with this id not found");
		}
		return ResponseEntity.ok().body(guestService.getUserInfoById(id));
	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		return ResponseEntity.ok().body(guestService.getAllUsers());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		if (id == null || id <= 0) {
			return ResponseEntity.badRequest().body("Invalid id");
		}
		return ResponseEntity.ok().body(guestService.deleteUserById(id));
	}

	@GetMapping("/backup")
	public ResponseEntity<?> backup() {
		return ResponseEntity.ok().body(guestService.backupDatabase());
	}
}
