package com.rafael.atendimento.enums.converters;

import java.util.stream.Stream;

import com.rafael.atendimento.enums.ServiceStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ServiceStatusConverter implements AttributeConverter<ServiceStatus, String> {
	
	@Override
	public String convertToDatabaseColumn(ServiceStatus typeAcess) {
		if (typeAcess == null) {
			return null;
		}
		return typeAcess.getValue();
	}

	@Override
	public ServiceStatus convertToEntityAttribute(String value) {
		if (value == null) {
            return null;
        }
        return Stream.of(ServiceStatus.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
	}

}
