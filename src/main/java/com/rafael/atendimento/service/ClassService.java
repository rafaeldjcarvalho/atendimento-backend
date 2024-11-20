package com.rafael.atendimento.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.mapper.ClassMapper;
import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.repository.ClassRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassService {
	
	private final ClassRepository classRepository;
	private final UserService userService;
	private final ClassMapper classMapper;
	
	public List<ClassDTO> findAll() {
		return classRepository.findAll()
				.stream()
				.map(classMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	public ClassDTO findById(Long id) {
        Class turma = classRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
        return classMapper.toDTO(turma);
    }
	
	public ClassDTO create(Class classRequest) {
        Optional<Class> existingClass = classRepository.findByName(classRequest.getName());
        if (existingClass.isEmpty()) {
        	try {
        		User user = userService.findUserById(classRequest.getOwner().getId());
        		Class newClass = new Class();
        		newClass.setName(classRequest.getName());
        		newClass.setDate(classRequest.getDate());
        		newClass.setOwner(user);
                classRepository.save(newClass);
                return classMapper.toDTO(newClass);
        	} catch (Exception ex) {
        		throw new RuntimeException("Class not registred" + ex.getMessage());
        	}
            
        }
        throw new RuntimeException("Class already exists");
    }
	
	public ClassDTO update(Long id, Class classRequest) {
		Class existingClass = classRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
        		
		existingClass.setName(classRequest.getName());
		existingClass.setDate(classRequest.getDate());
        Class updatedClass = classRepository.save(existingClass);
        return classMapper.toDTO(updatedClass);
    }
	
	public void delete(Long id) {
        Class c = classRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        classRepository.delete(c);
    }
	
	public ClassDTO addUserInClass(Long classId, Long userId) {
		Class existingClass = classRepository.findById(classId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
		User existingUser = userService.findUserById(userId);
		
		existingClass.addUser(existingUser);
		Class newClass = classRepository.save(existingClass);
		return classMapper.toDTO(newClass);
	}
	
	//Antes de deletar o usuário da turma é necessário alterar o tipo de usuário
	public ClassDTO deleteUserInClass(Long classId, Long userId) {
		Class existingClass = classRepository.findById(classId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
		User existingUser = userService.findUserById(userId);
		
		existingClass.removeUser(existingUser);
		Class newClass = classRepository.save(existingClass);
		return classMapper.toDTO(newClass);
	}
	
	//Atualizar usuário na turma - mudar status de aluno para monitor
	

}
