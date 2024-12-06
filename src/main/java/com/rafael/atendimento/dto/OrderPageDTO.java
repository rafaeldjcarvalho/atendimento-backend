package com.rafael.atendimento.dto;

import java.util.List;

public record OrderPageDTO (
		List<OrderServiceDTO> orders,
		Long totalElementos,
		int totalPages) {}
