package com.rafael.atendimento.dto;

import java.util.List;

public record CalendarDTO (
		Long id,
		String ownerName,
		Long ownerId,
		List<ScheduleDTO> schedules
		) {}
