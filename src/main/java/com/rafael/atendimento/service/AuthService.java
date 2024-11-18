package com.rafael.atendimento.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rafael.atendimento.dto.LoginRequestDTO;
import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.ResponseDTO;
import com.rafael.atendimento.dto.mapper.UserMapper;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.infra.security.TokenService;
import com.rafael.atendimento.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository repository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final TokenService tokenService;
	
	public ResponseDTO login(LoginRequestDTO userRequest) {
        User user = repository.findByEmail(userRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(userRequest.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return new ResponseDTO(user.getName(), token);
        }
        throw new RuntimeException("Invalid credentials");
    }
	
	public ResponseDTO register(RegisterRequestDTO userRequest) {
        Optional<User> user = repository.findByEmail(userRequest.email());
        if (user.isEmpty()) {
        	try {
        		User newUser = new User();
                newUser.setPassword(passwordEncoder.encode(userRequest.password()));
                newUser.setEmail(userRequest.email());
                newUser.setName(userRequest.name());
                newUser.setTypeAccess(userMapper.convertTypeAccessValue(userRequest.typeAccess()));
                repository.save(newUser);
                String token = tokenService.generateToken(newUser);
                return new ResponseDTO(newUser.getName(), token);
        	} catch (Exception ex) {
        		throw new RuntimeException("User not registred");
        	}
            
        }
        throw new RuntimeException("User already exists");
    }
}
