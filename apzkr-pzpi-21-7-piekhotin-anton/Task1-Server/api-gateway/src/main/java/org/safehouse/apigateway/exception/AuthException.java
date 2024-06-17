package org.safehouse.apigateway.exception;

public class AuthException extends SafeHouseException {

	public AuthException(String msg, String errorCode) {
		super(msg, errorCode);
	}
}
