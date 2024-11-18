package com.rafael.atendimento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.repository.ClassRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassService {
	
	private final ClassRepository classRepository;
	
	public List<Class> findAll() {
		return classRepository.findAll();
	}
	
	public Class findById(Long id) {
        return classRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
    }
	
	public Class create(Class classRequest) {
        Optional<Class> existingClass = classRepository.findByName(classRequest.getName());
        if (existingClass.isEmpty()) {
        	try {
        		Class newClass = new Class();
        		newClass.setName(classRequest.getName());
        		newClass.setDate(classRequest.getDate());
                return classRepository.save(newClass);
        	} catch (Exception ex) {
        		throw new RuntimeException("Class not registred");
        	}
            
        }
        throw new RuntimeException("Class already exists");
    }
	
	public Class update(Long id, Class classRequest) {
		Class existingClass = classRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
        		
		existingClass.setName(classRequest.getName());
		existingClass.setDate(classRequest.getDate());
        return classRepository.save(existingClass);
    }
	
	public void delete(Long id) {
        Class c = classRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        classRepository.delete(c);
    }

}
