package org.safehouse.authservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.safehouse.authservice.service.security.GuestDetailsService;
import org.safehouse.authservice.service.security.RequestMetaInfoFilter;
import org.safehouse.authservice.service.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

	private final PasswordEncoder passwordEncoder;
	private final GuestDetailsService guestDetailsService;
	private final JwtAuthFilter jwtAuthFilter;
	private final RequestMetaInfoFilter requestMetaInfoFilter;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(cors -> {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(List.of("https://safehouse-proj.azurewebsites.net"));
			config.setAllowedHeaders(List.of("*"));
			config.setAllowedMethods(List.of("*"));
			cors.configurationSource(request -> config);
		});
		http.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);
		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(requestMetaInfoFilter, JwtAuthFilter.class);

		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
				.requestMatchers("/api/v1/auth/**").permitAll()
		);

		http.exceptionHandling((exception) -> exception.authenticationEntryPoint(customAuthenticationEntryPoint));

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(guestDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}

}
