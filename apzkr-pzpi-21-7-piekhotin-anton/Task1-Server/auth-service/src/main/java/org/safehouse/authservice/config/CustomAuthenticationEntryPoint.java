package org.safehouse.authservice.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.safehouse.authservice.model.exception.NullOrEmptyJwtException;
import org.safehouse.authservice.service.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private static final String AUTHORIZATION_NAME = "Authorization";
	private static final String BEARER_NAME = "Bearer ";
	private static final int INDEX_TOKEN_FROM_BEARER = 7;

	private final JwtUtil jwtUtil;

	private final HandlerExceptionResolver resolver;

	@Autowired
	public CustomAuthenticationEntryPoint(JwtUtil jwtUtil, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
		this.jwtUtil = jwtUtil;
		this.resolver = resolver;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) {
		try {
			jwtUtil.extractExpiration(extractToken(request));
		} catch (IllegalArgumentException exception) {
			resolver.resolveException(request, response, null, new NullOrEmptyJwtException(exception.getMessage()));
			return;
		} catch (SignatureException | ExpiredJwtException exception) {
			resolver.resolveException(request, response, null, exception);
			return;
		}

		resolver.resolveException(request, response, null, authException);
	}

	private String extractToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_NAME);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_NAME)) {
			return bearerToken.substring(INDEX_TOKEN_FROM_BEARER);
		}
		return null;
	}
}
