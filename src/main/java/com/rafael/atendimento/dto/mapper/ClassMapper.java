package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.entity.Class;

@Component
public class ClassMapper {
	
	public ClassDTO toDTO(Class turma) {
	    if (turma == null) {
	        return null;
	    }
	    return new ClassDTO(
	        turma.getId(),
	        turma.getName(),
	        turma.getDate()
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
	    
	    return classes;
	}
}
