package com.thelocalclass.utm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidRequestException extends ResponseStatusException {
	
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(HttpStatus status) {
		super(status);
		
	}

	public InvalidRequestException(HttpStatus status, String reason) {
		super(status, reason);
		
	}
	
	public InvalidRequestException(HttpStatus status, String reason, Throwable cause) {
		super(status, reason, cause);
	}
	
}
