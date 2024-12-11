package com.rafael.atendimento.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.UpdateRequestDTO;
import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping
	public List<UserDTO> findAll() {
		return userService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		UserDTO user = userService.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterRequestDTO userRequest) {
       try {
    	   UserDTO user = userService.create(userRequest);
    	   return ResponseEntity.ok(user);
       } catch (RuntimeException ex) {
    	   return ResponseEntity.badRequest().body(ex.getMessage());
       }
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateRequestDTO userRequest) {
	   UserDTO user = userService.update(id, userRequest);
	   return ResponseEntity.ok(user);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	@GetMapping("/user-class/{classId}")
    public ResponseEntity<?> getUserClasses(@PathVariable Long classId) {
		try {
			List<UserDTO> users = userService.getAllStudentsInClass(classId);
	        return ResponseEntity.ok(users);
		} catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }

}
