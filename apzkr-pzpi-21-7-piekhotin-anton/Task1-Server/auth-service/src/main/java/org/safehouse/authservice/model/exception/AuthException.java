package org.safehouse.authservice.model.exception;

public class AuthException extends RuntimeException {

	private final String code;

	public AuthException(String msg, String errorCode) {
		super(msg);
		this.code = errorCode;
	}

	public String getCode() {
		return code;
	}
}
