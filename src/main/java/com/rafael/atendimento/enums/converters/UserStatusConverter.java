package com.rafael.atendimento.enums.converters;

import java.util.stream.Stream;

import com.rafael.atendimento.enums.UserStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {
	
	@Override
	public String convertToDatabaseColumn(UserStatus userStatus) {
		if (userStatus == null) {
			return null;
		}
		return userStatus.getValue();
	}

	@Override
	public UserStatus convertToEntityAttribute(String value) {
		if (value == null) {
            return null;
        }
        return Stream.of(UserStatus.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
	}
}
