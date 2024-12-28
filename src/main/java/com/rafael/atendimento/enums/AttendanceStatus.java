package com.rafael.atendimento.enums;

public enum AttendanceStatus {
	PRESENTE("Presente"), AUSENTE("Ausente");
	
private String value;
	
	private AttendanceStatus(String value) {
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
