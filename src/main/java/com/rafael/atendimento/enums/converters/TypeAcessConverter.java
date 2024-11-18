package com.rafael.atendimento.enums.converters;

import java.util.stream.Stream;

import com.rafael.atendimento.enums.TypeAccess;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeAcessConverter implements AttributeConverter<TypeAccess, String> {

	@Override
	public String convertToDatabaseColumn(TypeAccess typeAcess) {
		if (typeAcess == null) {
			return null;
		}
		return typeAcess.getValue();
	}

	@Override
	public TypeAccess convertToEntityAttribute(String value) {
		if (value == null) {
            return null;
        }
        return Stream.of(TypeAccess.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
	}

}
