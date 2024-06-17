package org.safehouse.apigateway.exception;

import org.springframework.http.HttpStatus;

public class JwtException extends SafeHouseException {
	public JwtException(String msg) {
		super(msg, HttpStatus.UNAUTHORIZED.toString());
	}
}

