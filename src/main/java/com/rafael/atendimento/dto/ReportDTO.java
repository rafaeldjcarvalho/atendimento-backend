package com.rafael.atendimento.dto;

import java.util.List;
import java.util.Map;

public record ReportDTO (
		Long totalServices,
		Long completedServices,
		Long canceledServices,
		List<ServiceDayDTO> servicesDays,
		Map<String, WeeklyHoursDTO> weeklyUsage
		) {}
