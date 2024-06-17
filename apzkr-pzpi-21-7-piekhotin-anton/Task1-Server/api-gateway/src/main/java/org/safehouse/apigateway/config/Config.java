package org.safehouse.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.safehouse.apigateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class Config {

	private final AuthenticationFilter filter;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("auth-service", r -> r.path("/api/v1/auth/**")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:8181"))
				.route("house-service", r -> r.path("/api/v1/house/**")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:8081"))
				.route("user-service", r -> r.path("/api/v1/user/**")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:8082"))
				.route("device-service", r -> r.path("/api/v1/device/**")
						.filters(f -> f.filter(filter))
						.uri("http://localhost:8083"))
				.build();
	}
}
