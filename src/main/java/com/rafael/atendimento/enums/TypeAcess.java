package com.rafael.atendimento.enums;

public enum TypeAcess {
	ALUNO("Aluno"), MONITOR("Monitor"), PROFESSOR("Professor"), ADMIN("Admin");
	
	private String value;
	
	private TypeAcess(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
