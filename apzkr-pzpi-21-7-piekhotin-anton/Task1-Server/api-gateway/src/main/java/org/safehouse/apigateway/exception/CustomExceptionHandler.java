package org.safehouse.apigateway.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class CustomExceptionHandler {

	private final String INVALID_JWT_TOKEN_MESSAGE = "Invalid JWT token: ";
	private final String EXPIRED_JWT_TOKEN_MESSAGE = "JWT token expired: ";

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ResponseErrorDto> handleInvalidJwtTokenException(SignatureException ex) {
		ResponseErrorDto errorDto = ResponseErrorDto.builder()
				.time(LocalDateTime.now())
				.statusCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
				.errorMessage(List.of(INVALID_JWT_TOKEN_MESSAGE + ex.getMessage()))
				.build();
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
	}

	@ExceptionHandler(value = {ExpiredJwtException.class})
	protected ResponseEntity<ResponseErrorDto> handlerExpiredJwtException(ExpiredJwtException ex) {
		ResponseErrorDto errorDto = ResponseErrorDto.builder()
				.time(LocalDateTime.now())
				.statusCode(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
				.errorMessage(List.of(EXPIRED_JWT_TOKEN_MESSAGE, ex.getMessage()))
				.build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	public ResponseEntity<ResponseErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
		ResponseErrorDto errorDto = ResponseErrorDto.builder()
				.time(LocalDateTime.now())
				.statusCode(String.valueOf(HttpStatus.FORBIDDEN.value()))
				.errorMessage(List.of(ex.getMessage()))
				.build();
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
	}

}
