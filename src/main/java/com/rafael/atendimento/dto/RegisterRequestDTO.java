package com.rafael.atendimento.dto;

import org.hibernate.validator.constraints.Length;

import com.rafael.atendimento.enums.TypeAcess;
import com.rafael.atendimento.enums.UserStatus;
import com.rafael.atendimento.enums.validation.ValueOfEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequestDTO (
		@NotBlank @NotNull @Length(min = 3, max = 100) String name, 
		@NotBlank @NotNull @Length(min = 5, max = 100) String email, 
		@NotBlank @NotNull @Length(min = 6, max = 100) String password, 
		@NotNull @Length(max = 10) @ValueOfEnum(enumClass = TypeAcess.class) String typeAcess
		) {}
