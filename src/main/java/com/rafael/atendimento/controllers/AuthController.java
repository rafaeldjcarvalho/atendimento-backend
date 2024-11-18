package com.rafael.atendimento.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.LoginRequestDTO;
import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.ResponseDTO;
import com.rafael.atendimento.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest){
        try {
        	ResponseDTO response = authService.login(loginRequest);
        	return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest){
    	try {
    		ResponseDTO response = authService.register(registerRequest);
    		return ResponseEntity.ok(response);
    	} catch (RuntimeException ex) {
    		return ResponseEntity.badRequest().body(ex.getMessage());
    	}
    }

}
