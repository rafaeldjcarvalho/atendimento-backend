package com.rafael.atendimento.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PasswordResetTokenService {

	private final Map<String, PasswordResetToken> tokens = new HashMap<>();

	public String createToken(String email) {
		String token = UUID.randomUUID().toString();
		tokens.put(token, new PasswordResetToken(email, LocalDateTime.now().plusHours(1))); // Expira em 1h
		return token;
	}
	
	public String getEmailByToken(String token) {
        PasswordResetToken resetToken = tokens.get(token);
        if (resetToken != null && resetToken.expiry.isAfter(LocalDateTime.now())) {
            return resetToken.email;
        }
        return null;
    }
	
	public void removeToken(String token) {
        tokens.remove(token);
    }

    private record PasswordResetToken(String email, LocalDateTime expiry) {}

}
