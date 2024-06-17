package org.safehouse.authservice.model.exception;


import org.springframework.http.HttpStatus;

public class NullOrEmptyJwtException extends RuntimeException {
	private final String code;

	public NullOrEmptyJwtException(String msg) {
		super(msg);
		this.code = HttpStatus.UNAUTHORIZED.toString();
	}

	public String getCode() {
		return code;
	}
}
