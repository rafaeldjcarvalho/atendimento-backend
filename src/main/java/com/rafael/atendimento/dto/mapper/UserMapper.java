package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.enums.TypeAcess;
import com.rafael.atendimento.enums.UserStatus;

@Component
public class UserMapper {
	
	public TypeAcess convertTypeAcessValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "Aluno" -> TypeAcess.ALUNO;
            case "Professor" -> TypeAcess.PROFESSOR;
            case "Monitor" -> TypeAcess.MONITOR;
            case "Admin" -> TypeAcess.ADMIN;
            default -> throw new IllegalArgumentException("Categoria inválida: " + value);
        };
    }
	
	public UserStatus convertUserStatusValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "Ativo" -> UserStatus.ATIVO;
            case "Suspenso" -> UserStatus.SUSPENSO;
            case "Inativo" -> UserStatus.INATIVO;
            default -> throw new IllegalArgumentException("Categoria inválida: " + value);
        };
    }

}
