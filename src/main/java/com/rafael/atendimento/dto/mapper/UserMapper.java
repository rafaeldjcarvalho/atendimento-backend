package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;

@Component
public class UserMapper {
	
	public UserDTO toDTO(User user) {
	    if (user == null) {
	        return null;
	    }
	    return new UserDTO(
	        user.getId(),
	        user.getName(),
	        user.getEmail(),
	        user.getTypeAccess(),
	        user.getStatus()
	    );
	}
	
	public User toEntity(UserDTO userDTO) {
	    if (userDTO == null) {
	        return null;
	    }

	    User user = new User();
	    if (userDTO.id() != null) {
	        user.setId(userDTO.id());
	    }
	    user.setName(userDTO.name());
	    user.setEmail(userDTO.email());
	    user.setTypeAccess(userDTO.typeAccess());
	    user.setStatus(userDTO.status());
	    
	    return user;
	}
	
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
