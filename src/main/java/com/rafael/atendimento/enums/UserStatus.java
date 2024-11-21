package com.rafael.atendimento.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
	ATIVO("Ativo"), SUSPENSO("Suspenso"), INATIVO("Inativo");
	
	private String value;
	
	private UserStatus(String value) {
		this.value = value;
	}
	
	@JsonValue
	public String getValue() {
		return value;
	}
	
	@JsonCreator
	public static UserStatus fromValue(String value) {
		for (UserStatus status : UserStatus.values()) {
			if (status.value.equalsIgnoreCase(value)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Invalid status value: " + value);
	}
}
