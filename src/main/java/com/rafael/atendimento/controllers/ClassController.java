package com.rafael.atendimento.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.CalendarDTO;
import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.ClassPageDTO;
import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.service.CalendarService;
import com.rafael.atendimento.service.ClassService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
	
	private final ClassService classService;
	private final CalendarService calendarService;
	
//	@GetMapping
//	public List<ClassDTO> findAll() {
//		return classService.findAll();
//	}
	
	@GetMapping
    public ClassPageDTO list(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return classService.list(page, pageSize);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<ClassDTO> findById(@PathVariable Long id) {
		ClassDTO findClass = classService.findById(id);
		return ResponseEntity.ok(findClass);
	}
	
	@PostMapping
    public ResponseEntity<?> createClass(@RequestBody @Valid ClassDTO classRequest) {
       try {
    	   ClassDTO findClass = classService.create(classRequest);
    	   return ResponseEntity.ok(findClass);
       } catch (RuntimeException ex) {
    	   return ResponseEntity.badRequest().body(ex.getMessage());
       }
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateClass(@PathVariable Long id, @RequestBody @Valid Class classRequest) {
		try {
			ClassDTO findClass = classService.update(id, classRequest);
			return ResponseEntity.ok(findClass);
		} catch (RuntimeException ex) {
	    	return ResponseEntity.badRequest().body(ex.getMessage());
	    }
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(@PathVariable Long id) {
		classService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	// Métodos de Relacionamtentos
	
	@PostMapping("/{classId}/add-user/{userId}")
	public ResponseEntity<?> addUserInClass(@PathVariable Long classId, @PathVariable Long userId) {
		try {
			ClassDTO updatedClass = classService.addUserInClass(classId, userId);
			return ResponseEntity.ok(updatedClass);
        } catch (RuntimeException ex) {
        	return ResponseEntity.badRequest().body(ex.getMessage());
        }
	}
	
	@DeleteMapping("/{classId}/remove-user/{userId}")
	public ResponseEntity<?> removeUserInClass(@PathVariable Long classId, @PathVariable Long userId) {
		try {
			ClassDTO updatedClass = classService.deleteUserInClass(classId, userId);
			return ResponseEntity.ok(updatedClass);
        } catch (RuntimeException ex) {
        	return ResponseEntity.badRequest().body(ex.getMessage());
        }
	}
	
	@GetMapping("/class-user/{userId}")
    public ResponseEntity<?> getUserClasses(@PathVariable Long userId) {
		try {
			List<ClassDTO> classes = classService.getClassesForUser(userId);
	        return ResponseEntity.ok(classes);
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }
	
	// Criar um novo calendário para a turma
	@PostMapping("/{classId}/calendars")
    public ResponseEntity<?> createCalendar(@PathVariable Long classId, @RequestBody @Valid CalendarDTO calendarDTO) {
        try {
            CalendarDTO newCalendar = calendarService.createCalendar(classId, calendarDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCalendar);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
	
	// Listar todos os calendários de uma turma
    @GetMapping("/{classId}/calendars")
    public ResponseEntity<List<CalendarDTO>> listCalendars(@PathVariable Long classId) {
        List<CalendarDTO> calendars = calendarService.getCalendarsByClassId(classId);
        return ResponseEntity.ok(calendars);
    }
    
 // Deletar um calendário
    @DeleteMapping("/{classId}/calendars/{calendarId}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long classId, @PathVariable Long calendarId) {
        calendarService.deleteCalendar(classId, calendarId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{classId}/promote/{userId}")
    public ResponseEntity<?> promoteToMonitor(@PathVariable Long classId, @PathVariable Long userId) {
    	try {
    		classService.promoteToMonitor(classId, userId);
    		return ResponseEntity.ok().build();
    	} catch (RuntimeException ex) {
    		return ResponseEntity.badRequest().body(ex.getMessage());
    	}
    }
    
    @PutMapping("/{classId}/demote/{userId}")
    public ResponseEntity<?> demoteToStudent(@PathVariable Long classId, @PathVariable Long userId) {
    	try {
    		classService.demoteToStudent(classId, userId);
    		return ResponseEntity.ok().build();
    	} catch (RuntimeException ex) {
    		return ResponseEntity.badRequest().body(ex.getMessage());
    	}
    }
}
