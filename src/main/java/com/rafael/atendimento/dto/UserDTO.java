package com.rafael.atendimento.dto;

import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;

public record UserDTO (
		Long id,
		String name,
		String email,
		TypeAccess typeAccess,
		UserStatus status) {}
