package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.entity.Class;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClassMapper {
	
	private final UserMapper userMapper;
	
	public ClassDTO toDTO(Class turma) {
	    if (turma == null) {
	        return null;
	    }
	    return new ClassDTO(
	        turma.getId(),
	        turma.getName(),
	        turma.getDate(),
	        userMapper.toDTO(turma.getOwner())
	    );
	}
	
	public Class toEntity(ClassDTO classDTO) {
	    if (classDTO == null) {
	        return null;
	    }

	    Class classes = new Class();
	    if (classDTO.id() != null) {
	    	classes.setId(classDTO.id());
	    }
	    classes.setName(classDTO.name());
	    classes.setDate(classDTO.date());
	    classes.setOwner(userMapper.toEntity(classDTO.owner()));
	    
	    return classes;
	}
}
