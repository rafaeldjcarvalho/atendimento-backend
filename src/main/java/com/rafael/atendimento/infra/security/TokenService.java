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
import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.dto.mapper.UserMapper;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.exception.AccessDeniedException;
import com.rafael.atendimento.exception.AccountInactiveException;
import com.rafael.atendimento.exception.InvalidTokenException;
import com.rafael.atendimento.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
	
	private final UserRepository userRepository;
	private final UserMapper mapper;
	
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
	        String[] splitToken = token.split(" ");

	        // Decodifica e valida o token
	        String userSubject = JWT.require(algorithm)
	                .withIssuer("login-auth-api")
	                .build()
	                .verify(splitToken[1])
	                .getSubject();

	        // Obtém informações do token
	        //String userId = decodedToken.getClaim("id").asString();
	        //String role = decodedToken.getClaim("access").asString();

	        // Consulta os dados do usuário no banco
	        UserDTO user = mapper.toDTO(userRepository.findByEmail(userSubject)
	                .orElseThrow(() -> new RuntimeException("Usuário não encontrado.")));

	        // Valida se o perfil no banco corresponde ao perfil do token
	        //if (!user.access().equals(role)) {
	        //    throw new RuntimeException("Inconsistência de perfis: o token não corresponde ao perfil do usuário.");
	        //}

	        // Valida se o perfil está permitido
	        if (allowedRoles != null && !allowedRoles.contains(user.access())) {
	            throw new AccessDeniedException("Acesso negado para o perfil: " + user.access());
	        }

	        // Valida o status do usuário
	        if (!"Ativo".equals(user.status()) && !allowInactive) {
	            throw new AccountInactiveException("A conta está inativa ou suspensa.");
	        }

	        return true;
	    } catch (JWTVerificationException exception) {
	        throw new InvalidTokenException("Token inválido ou expirado.");
	    } catch (NumberFormatException e) {
	        throw new RuntimeException("Formato de ID de usuário inválido no token.", e);
	    }
	}

	
	private Instant generateExpirationDate(){
        //return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
		return LocalDateTime.now().plusMinutes(10).toInstant(ZoneOffset.of("-03:00"));
    }
}
