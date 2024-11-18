package com.rafael.atendimento.enums;

public enum UserStatus {
	ATIVO("Ativo"), SUSPENSO("Suspenso"), INATIVO("Inativo");
	
	private String value;
	
	private UserStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}