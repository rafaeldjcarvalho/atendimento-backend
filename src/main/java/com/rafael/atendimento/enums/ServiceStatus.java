package com.rafael.atendimento.enums;

public enum ServiceStatus {
	PENDENTE("Pendente"), CONCLUIDO("Concluido"), CANCELADO("Cancelado");
	
	private String value;
	
	private ServiceStatus(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
    public String toString() {
        return value;
    }
}
