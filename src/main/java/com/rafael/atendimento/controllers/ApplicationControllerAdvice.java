package com.rafael.atendimento.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rafael.atendimento.exception.AccessDeniedException;
import com.rafael.atendimento.exception.AccountInactiveException;
import com.rafael.atendimento.exception.InvalidTokenException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	@ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage(), "type", "ACCESS_DENIED"));
    }

    @ExceptionHandler(AccountInactiveException.class)
    public ResponseEntity<Map<String, String>> handleAccountInactiveException(AccountInactiveException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage(), "type", "ACCOUNT_INACTIVE"));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage(), "type", "INVALID_TOKEN"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", ex.getMessage(), "type", "GENERAL_ERROR"));
    }
}
