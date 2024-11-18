package com.rafael.atendimento.enums;

public enum TypeAccess {
	ALUNO("Aluno"), MONITOR("Monitor"), PROFESSOR("Professor"), ADMIN("Admin");
	
	private String value;
	
	private TypeAccess(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
