package com.rafael.atendimento.dto;

import java.util.List;

public record ClassPageDTO (
		List<ClassDTO> classes,
		Long totalElementos,
		int totalPages) {}
