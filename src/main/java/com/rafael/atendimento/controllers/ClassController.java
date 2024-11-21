package com.rafael.atendimento.controllers;

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

import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.ClassPageDTO;
import com.rafael.atendimento.entity.Class;
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

}
