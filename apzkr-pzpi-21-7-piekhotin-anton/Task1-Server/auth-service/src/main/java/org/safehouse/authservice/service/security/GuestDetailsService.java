package org.safehouse.authservice.service.security;

import lombok.RequiredArgsConstructor;
import org.safehouse.authservice.model.entity.Guest;
import org.safehouse.authservice.service.GuestService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GuestDetailsService implements UserDetailsService {

	private final GuestService guestService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Guest guest = guestService.findByEmail(username);
		return UserDetailsImpl.build(guest);
	}
}
