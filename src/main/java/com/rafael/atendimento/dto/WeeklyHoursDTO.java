package com.rafael.atendimento.dto;

import java.util.Map;

public record WeeklyHoursDTO (
		String userName,
		Map<String, Double> hoursByWeek) {}