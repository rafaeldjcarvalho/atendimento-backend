package com.rafael.atendimento.dto;

import java.util.List;

public record CustomerPageDTO (
		List<CustomerServiceDTO> services,
		Long totalElementos,
		int totalPages){}
