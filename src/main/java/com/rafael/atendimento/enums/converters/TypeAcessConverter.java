package com.rafael.atendimento.enums.converters;

import java.util.stream.Stream;

import com.rafael.atendimento.enums.TypeAcess;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeAcessConverter implements AttributeConverter<TypeAcess, String> {

	@Override
	public String convertToDatabaseColumn(TypeAcess typeAcess) {
		if (typeAcess == null) {
			return null;
		}
		return typeAcess.getValue();
	}

	@Override
	public TypeAcess convertToEntityAttribute(String value) {
		if (value == null) {
            return null;
        }
        return Stream.of(TypeAcess.values())
                .filter(c -> c.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
	}

}
