package com.rafael.atendimento.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.LoginRequestDTO;
import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.ResponseDTO;
import com.rafael.atendimento.service.AuthService;
import com.rafael.atendimento.service.EmailService;
import com.rafael.atendimento.service.PasswordResetTokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final EmailService emailService;
    private final PasswordResetTokenService tokenService;
	
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest){
    	ResponseDTO response = authService.login(loginRequest);
    	return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest){
		ResponseDTO response = authService.register(registerRequest);
		return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        String token = tokenService.createToken(email);
        emailService.sendResetPasswordEmail(email, token);
        return ResponseEntity.ok("E-mail de redefinição enviado.");
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody String newPassword) {
        String email = tokenService.getEmailByToken(token);
        if (email == null) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }

        // Aqui você deve atualizar a senha do usuário no banco de dados
        authService.updatePassword(email, newPassword);

        tokenService.removeToken(token);
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }

}
