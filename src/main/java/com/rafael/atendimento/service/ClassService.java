package com.rafael.atendimento.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.ClassPageDTO;
import com.rafael.atendimento.dto.mapper.ClassMapper;
import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.repository.ClassRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClassService {
	
	private final ClassRepository classRepository;
	private final UserService userService;
	private final ClassMapper classMapper;
	private final NotificationService notificationService;
	
//	public List<ClassDTO> findAll() {
//		return classRepository.findAll()
//				.stream()
//				.map(classMapper::toDTO)
//				.collect(Collectors.toList());
//	}
	
	public ClassPageDTO list(@PositiveOrZero int page, @Positive @Max(100) int pageSize) {
        Page<Class> pageClass = classRepository.findAll(PageRequest.of(page, pageSize));
        List<ClassDTO> classes = pageClass.get().map(classMapper::toDTO).collect(Collectors.toList());
        return new ClassPageDTO(classes, pageClass.getTotalElements(), pageClass.getTotalPages());
    }
	
	public ClassDTO findById(Long id) {
        Class turma = classRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
        return classMapper.toDTO(turma);
    }
	
	public Class findClassById(Long id) {
        Class turma = classRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
        return turma;
    }
	
	public ClassDTO create(ClassDTO classRequest) {
        Optional<Class> existingClass = classRepository.findByName(classRequest.name());
        if (existingClass.isEmpty()) {
        	try {
        		//User user = userService.findUserById(classRequest.getOwner().getId());
        		User user = userService.findByEmail(classRequest.owner().email());
        		Class newClass = new Class();
        		newClass.setName(classRequest.name());
        		newClass.setDate(classRequest.date());
        		newClass.setOwner(user);
        		newClass.addUser(user);
                classRepository.save(newClass);
                return classMapper.toDTO(newClass);
        	} catch (Exception ex) {
        		throw new RuntimeException("Class not registred" + ex.getMessage());
        	}
            
        }
        throw new RuntimeException("Class already exists");
    }
	
	public ClassDTO update(Long id, ClassDTO classRequest) {
		Class existingClass = classRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
        		
		existingClass.setName(classRequest.name());
		existingClass.setDate(classRequest.date());
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
		
		if(!existingClass.getAlunos().contains(existingUser) || 
				!existingClass.getMonitores().contains(existingUser) || 
					!existingClass.getProfessores().contains(existingUser)) {
			existingClass.addUser(existingUser);
			Class newClass = classRepository.save(existingClass);
			return classMapper.toDTO(newClass);
		}
		throw new RuntimeException("user is already registered");
	}
	
	//Antes de deletar o usuário da turma é necessário alterar o tipo de usuário
	public ClassDTO deleteUserInClass(Long classId, Long userId) {
		Class existingClass = classRepository.findById(classId)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));
		User existingUser = userService.findUserById(userId);
		
		if(existingClass.getAlunos().contains(existingUser) || 
				existingClass.getMonitores().contains(existingUser) || 
					existingClass.getProfessores().contains(existingUser)) {
			existingClass.removeUser(existingUser);
			Class newClass = classRepository.save(existingClass);
			return classMapper.toDTO(newClass);
		}
		throw new RuntimeException("user is not registered");
	}
	
	//Atualizar usuário na turma - mudar status de aluno para monitor
	
	public void promoteToMonitor(Long classId, Long userId) {
		
		if (userService.isUserAlreadyMonitor(userId)) {
	        throw new RuntimeException("Este usuário já é monitor de uma turma.");
	    }
		
	    // Busca a turma
	    Class turma = classRepository.findById(classId)
	            .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

	    // Busca o aluno na lista de alunos da turma
	    User aluno = turma.getAlunos().stream()
	            .filter(user -> user.getId().equals(userId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Aluno não encontrado na turma."));

	    // Atualiza o status do aluno para MONITOR
	    aluno.setTypeAccess(TypeAccess.MONITOR);
	    aluno.setMonitorClass(turma);

	    // Move o aluno para a lista de monitores
	    turma.getAlunos().remove(aluno);
	    turma.getMonitores().add(aluno);

	    // Salva as alterações
	    userService.updateTypeOfUser(aluno);
	    classRepository.save(turma);
	    
	    notificationService.notifyUserBecameMonitor(aluno.getEmail(), turma.getName());
	}
	
	public void demoteToStudent(Long classId, Long userId) {
	    // Busca a turma
	    Class turma = classRepository.findById(classId)
	            .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

	    // Busca o monitor na lista de monitores da turma
	    User monitor = turma.getMonitores().stream()
	            .filter(user -> user.getId().equals(userId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Monitor não encontrado na turma."));

	    // Atualiza o status do monitor para ALUNO
	    monitor.setTypeAccess(TypeAccess.ALUNO);
	    monitor.setMonitorClass(null);

	    // Move o monitor para a lista de alunos
	    turma.getMonitores().remove(monitor);
	    turma.getAlunos().add(monitor);

	    // Salva as alterações
	    userService.updateTypeOfUser(monitor);
	    classRepository.save(turma);
	    
	    notificationService.notifyUserRemoveMonitor(monitor.getEmail(), turma.getName());
	}
	
	public List<ClassDTO> getClassesForUser(Long userId) {
        userService.findUserById(userId);

        List<ClassDTO> classes = new ArrayList<>();
        classes.addAll(classRepository.findClassesByAlunoId(userId)
        		.stream()
        		.map(classMapper::toDTO)
        		.collect(Collectors.toList()));
        classes.addAll(classRepository.findClassesByProfessorId(userId)
        		.stream()
        		.map(classMapper::toDTO)
        		.collect(Collectors.toList()));
        classes.addAll(classRepository.findClassesByMonitorId(userId)
        		.stream()
        		.map(classMapper::toDTO)
        		.collect(Collectors.toList()));
        return classes;
    }
}
