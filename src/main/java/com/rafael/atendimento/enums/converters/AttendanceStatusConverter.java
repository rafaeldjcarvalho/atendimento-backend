package com.rafael.atendimento.enums.converters;

import java.util.stream.Stream;

import com.rafael.atendimento.enums.AttendanceStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AttendanceStatusConverter implements AttributeConverter<AttendanceStatus, String> {
	
	@Override
	public String convertToDatabaseColumn(AttendanceStatus attendanceStatus) {
		if (attendanceStatus == null) {
			return null;
		}
		return attendanceStatus.getValue();
	}

	@Override
	public AttendanceStatus convertToEntityAttribute(String value) {
		if (value == null) {
            return null;
        }
        return Stream.of(AttendanceStatus.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
	}
}
