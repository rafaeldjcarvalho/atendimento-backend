package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;

@Component
public class UserMapper {
	
	public TypeAccess convertTypeAccessValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "Aluno" -> TypeAccess.ALUNO;
            case "Professor" -> TypeAccess.PROFESSOR;
            case "Monitor" -> TypeAccess.MONITOR;
            case "Admin" -> TypeAccess.ADMIN;
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
