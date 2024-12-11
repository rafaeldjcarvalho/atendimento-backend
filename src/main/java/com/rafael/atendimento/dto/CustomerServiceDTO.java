package com.rafael.atendimento.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.validator.constraints.Length;

import com.rafael.atendimento.enums.ServiceStatus;
import com.rafael.atendimento.enums.validation.ValueOfEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerServiceDTO (
		Long id,
		@NotBlank @NotNull @Length(min = 3, max = 100) String title,
		@NotBlank @NotNull @Length(min = 3, max = 100) String description,
		@NotNull LocalDate date,
		@NotNull LocalTime time_start,
		@NotNull LocalTime time_end,
		@NotNull @Length(max = 15) @ValueOfEnum(enumClass = ServiceStatus.class) String status,
		@NotNull Long classId,
		@NotNull Long userId,
		@NotNull Long studentId) {}
