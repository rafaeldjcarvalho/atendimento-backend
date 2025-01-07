package com.rafael.atendimento.dto;

import java.time.LocalDate;

public record ServiceDayDTO (
		LocalDate date,
		double hours) {}
