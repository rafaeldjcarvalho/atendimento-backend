package com.rafael.atendimento.dto;

import java.time.LocalDate;

public record ClassDTO (
		Long id,
		String name,
		LocalDate date,
		UserDTO owner) {}
