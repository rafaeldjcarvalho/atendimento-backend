package com.rafael.atendimento.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.ResponseDTO;
import com.rafael.atendimento.dto.UpdateRequestDTO;
import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.infra.security.TokenService;
import com.rafael.atendimento.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final TokenService tokenService;
	
	@GetMapping
	public List<UserDTO> findAll(@RequestHeader("Authorization") String token) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), false);
		return userService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findById(
			@RequestHeader("Authorization") String token, 
			@PathVariable Long id) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		UserDTO user = userService.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping
    public ResponseEntity<?> createUser(
    		@RequestHeader("Authorization") String token, 
    		@RequestBody RegisterRequestDTO userRequest) {
       try {
    	   tokenService.validateTokenAndPermissions(token, List.of("Admin", "Professor"), false);
    	   UserDTO user = userService.create(userRequest);
    	   return ResponseEntity.ok(user);
       } catch (RuntimeException ex) {
    	   return ResponseEntity.badRequest().body(ex.getMessage());
       }
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
    		@RequestHeader("Authorization") String token, 
    		@PathVariable Long id, 
    		@RequestBody UpdateRequestDTO userRequest) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		ResponseDTO user = userService.update(id, userRequest);
		return ResponseEntity.ok(user);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
    		@RequestHeader("Authorization") String token, 
    		@PathVariable Long id) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Professor"), false);
		userService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	@GetMapping("/user-class/{classId}")
    public ResponseEntity<?> getUserClasses(
    		@RequestHeader("Authorization") String token, 
    		@PathVariable Long classId) {
		try {
			tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
			List<UserDTO> users = userService.getAllStudentsInClass(classId);
	        return ResponseEntity.ok(users);
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }
	
	@GetMapping("/userTeachers-class/{classId}")
    public ResponseEntity<?> getUserTeachersClasses(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId) {
		try {
			tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
			List<UserDTO> users = userService.getAllTeachersInClass(classId);
	        return ResponseEntity.ok(users);
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }
	
	@GetMapping("/userMonitors-class/{classId}")
    public ResponseEntity<?> getUserMonitorsClasses(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId) {
		try {
			tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
			List<UserDTO> users = userService.getAllMonitorsInClass(classId);
	        return ResponseEntity.ok(users);
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }

}
