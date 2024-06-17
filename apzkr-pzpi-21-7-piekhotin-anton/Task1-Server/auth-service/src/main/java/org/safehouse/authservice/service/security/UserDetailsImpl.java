package org.safehouse.authservice.service.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.safehouse.authservice.model.entity.Guest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsImpl implements UserDetails {

	private final Long id;
	//email
	private final String username;
	private final String password;
	private final boolean enabled;
	private final Collection<? extends GrantedAuthority> authorities;

	public static UserDetailsImpl build(@NonNull Guest guest) {
		List<GrantedAuthority> authorityList = new ArrayList<>();
		if (guest.getRole() != null) {
			authorityList.add(new SimpleGrantedAuthority(guest.getRole().name()));
		}
		boolean isEnabled = false;
		if (guest.getIsEnabled() != null) {
			isEnabled = guest.getIsEnabled();
		}

		return new UserDetailsImpl(
				guest.getId(),
				guest.getEmail(),
				guest.getPassword(),
				isEnabled,
				authorityList
		);
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}

		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
