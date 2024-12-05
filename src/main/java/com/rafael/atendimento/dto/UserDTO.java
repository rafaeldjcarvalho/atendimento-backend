package com.rafael.atendimento.dto;

import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;
import com.rafael.atendimento.enums.validation.ValueOfEnum;

public record UserDTO (
		Long id,
		String name,
		String email,
		@ValueOfEnum(enumClass = TypeAccess.class) String typeAccess,
		@ValueOfEnum(enumClass = UserStatus.class) String status) {}
