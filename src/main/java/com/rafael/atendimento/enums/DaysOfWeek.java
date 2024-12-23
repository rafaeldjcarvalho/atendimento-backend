package com.rafael.atendimento.enums;

import java.time.DayOfWeek;

public enum DaysOfWeek {
	
	SEGUNDA, TERÇA, QUARTA, QUINTA, SEXTA, SÁBADO, DOMINGO;

    public static DaysOfWeek fromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return SEGUNDA;
            case TUESDAY: return TERÇA;
            case WEDNESDAY: return QUARTA;
            case THURSDAY: return QUINTA;
            case FRIDAY: return SEXTA;
            case SATURDAY: return SÁBADO;
            case SUNDAY: return DOMINGO;
            default: throw new IllegalArgumentException("Dia da semana inválido: " + dayOfWeek);
        }
    }

}
