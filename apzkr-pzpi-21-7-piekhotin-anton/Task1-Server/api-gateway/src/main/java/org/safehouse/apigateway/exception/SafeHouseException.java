package org.safehouse.apigateway.exception;

import lombok.Getter;

@Getter
public class SafeHouseException extends RuntimeException {

	protected String code;

	public SafeHouseException(String msg, String errorCode) {
		super(msg);
		this.code = errorCode;
	}
}
