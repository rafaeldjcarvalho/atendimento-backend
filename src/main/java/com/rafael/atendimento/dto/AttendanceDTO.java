package com.rafael.atendimento.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.enums.validation.ValueOfEnum;

public record AttendanceDTO (
		Long id,
		Long customerServiceId,
		Long userId,
		String userName,
		@ValueOfEnum(enumClass = AttendanceStatus.class) String status,
		LocalDate date,
		LocalTime time) {}
