package com.rafael.atendimento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.UpdateRequestDTO;
import com.rafael.atendimento.dto.mapper.UserMapper;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
	
	public User create(RegisterRequestDTO userRequest) {
        Optional<User> user = userRepository.findByEmail(userRequest.email());
        if (user.isEmpty()) {
        	try {
        		User newUser = new User();
                newUser.setPassword(passwordEncoder.encode(userRequest.password()));
                newUser.setEmail(userRequest.email());
                newUser.setName(userRequest.name());
                newUser.setTypeAccess(userMapper.convertTypeAccessValue(userRequest.typeAccess()));
                return userRepository.save(newUser);
        	} catch (Exception ex) {
        		throw new RuntimeException("User not registred");
        	}
            
        }
        throw new RuntimeException("User already exists");
    }
	
	public User update(Long id, UpdateRequestDTO userRequest) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        		
		existingUser.setPassword(passwordEncoder.encode(userRequest.password()));
		existingUser.setEmail(userRequest.email());
		existingUser.setName(userRequest.name());
		existingUser.setTypeAccess(userMapper.convertTypeAccessValue(userRequest.typeAccess()));
		existingUser.setStatus(userMapper.convertUserStatusValue(userRequest.status()));
        return userRepository.save(existingUser);
    }
	
	public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
    }
}
