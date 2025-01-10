package com.rafael.atendimento.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AccountInactiveException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AccountInactiveException(String mensagem) {
        super(mensagem);
    }
}
