package com.rafael.atendimento.dto;

import java.time.LocalTime;

public record ScheduleDTO (
		Long id,
		String dayOfWeek,
		LocalTime startTime,
		LocalTime endTime
		){}
