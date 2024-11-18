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
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping
	public List<User> findAll() {
		return userService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		User user = userService.findById(id);
		return ResponseEntity.ok(user);
	}
	
	@PostMapping
    public ResponseEntity<?> createUser(@RequestBody RegisterRequestDTO userRequest) {
       try {
    	   User user = userService.create(userRequest);
    	   return ResponseEntity.ok(user);
       } catch (RuntimeException ex) {
    	   return ResponseEntity.badRequest().body(ex.getMessage());
       }
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateRequestDTO userRequest) {
	   User user = userService.update(id, userRequest);
	   return ResponseEntity.ok(user);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
