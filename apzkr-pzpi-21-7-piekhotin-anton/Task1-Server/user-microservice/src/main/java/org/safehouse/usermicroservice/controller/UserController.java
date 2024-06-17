package org.safehouse.usermicroservice.controller;

import lombok.RequiredArgsConstructor;
import org.safehouse.usermicroservice.model.dto.GuestInfoDto;
import org.safehouse.usermicroservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping
	public String hello() {
		return "Hello world from user";
	}

	@PostMapping("/edit/{id}")
	public ResponseEntity<?> editUser(@RequestBody GuestInfoDto guestInfoDto, @PathVariable Long id) {
		if (guestInfoDto == null)
			return ResponseEntity.badRequest().body("Invalid data");

		return ResponseEntity.ok().body(userService.editUser(id, guestInfoDto));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Long id) {
		if (id == null || id <= 0)
			return ResponseEntity.badRequest().body("Invalid id");

		return ResponseEntity.ok().body(userService.getUserById(id));
	}

}
