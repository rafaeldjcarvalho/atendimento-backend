package com.rafael.atendimento.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rafael.atendimento.entity.User;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			String token = JWT.create().withIssuer("login-auth-api")
					.withSubject(user.getEmail())
					.withClaim("id", user.getId())
					.withClaim("name", user.getName())
					.withClaim("access", user.getTypeAccess().getValue())
					.withClaim("status", user.getStatus().getValue())
					.withExpiresAt(this.generateExpirationDate())
					.sign(algorithm);
			return token;
		} catch(JWTCreationException exception) {
			throw new RuntimeException("Error while Authenticating");
		}
	}
	
	public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
	
	public boolean validateTokenAndPermissions(String token, List<String> allowedRoles, boolean allowInactive) {
	    try {
	        Algorithm algorithm = Algorithm.HMAC256(secret);

	        // Decodifica e valida o token
	        DecodedJWT decodedToken = JWT.require(algorithm)
	                .withIssuer("login-auth-api")
	                .build()
	                .verify(token);

	        // Obtém informações do token
	        String role = decodedToken.getClaim("role").asString();
	        String status = decodedToken.getClaim("status").asString();

	        // Valida se o perfil está permitido
	        if (allowedRoles != null && !allowedRoles.contains(role)) {
	            throw new RuntimeException("Acesso negado para o perfil: " + role);
	        }

	        // Valida o status do usuário
	        if (!"Ativo".equals(status) && !allowInactive) {
	            throw new RuntimeException("A conta está inativa.");
	        }

	        return true;
	    } catch (JWTVerificationException exception) {
	        throw new RuntimeException("Token inválido ou expirado.", exception);
	    }
	}

	
	private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
