package com.rafael.atendimento.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.UpdateRequestDTO;
import com.rafael.atendimento.dto.UserDTO;
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
	
	public List<UserDTO> findAll() {
		return userRepository.findAll()
				.stream()
				.map(userMapper::toDTO)
				.collect(Collectors.toList());
	}
	
	public UserDTO findById(Long id) {
		User user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return userMapper.toDTO(user);
    }
	
	public User findUserById(Long id) {
		return userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
	
	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
	
	public UserDTO create(RegisterRequestDTO userRequest) {
        Optional<User> user = userRepository.findByEmail(userRequest.email());
        if (user.isEmpty()) {
        	try {
        		User newUser = new User();
                newUser.setPassword(passwordEncoder.encode(userRequest.password()));
                newUser.setEmail(userRequest.email());
                newUser.setName(userRequest.name());
                newUser.setTypeAccess(userMapper.convertTypeAccessValue(userRequest.typeAccess()));
                userRepository.save(newUser);
                return userMapper.toDTO(newUser);
        	} catch (Exception ex) {
        		throw new RuntimeException("User not registred");
        	}
            
        }
        throw new RuntimeException("User already exists");
    }
	
	public UserDTO update(Long id, UpdateRequestDTO userRequest) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        		
		existingUser.setPassword(passwordEncoder.encode(userRequest.password()));
		existingUser.setEmail(userRequest.email());
		existingUser.setName(userRequest.name());
		existingUser.setTypeAccess(userMapper.convertTypeAccessValue(userRequest.typeAccess()));
		existingUser.setStatus(userMapper.convertUserStatusValue(userRequest.status()));
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }
	
	public void delete(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(user);
    }
	
	public List<UserDTO> getAllStudentsInClass(Long classId) {
		List<User> users = userRepository.findStudentsByClassId(classId);
		List<UserDTO> usersDTO = users.stream().map(userMapper::toDTO).collect(Collectors.toList());
		return usersDTO;
	}
	
	public List<UserDTO> getAllTeachersInClass(Long classId) {
		List<User> users = userRepository.findTeachersByClassId(classId);
		List<UserDTO> usersDTO = users.stream().map(userMapper::toDTO).collect(Collectors.toList());
		return usersDTO;
	}
	
	public List<UserDTO> getAllMonitorsInClass(Long classId) {
		List<User> users = userRepository.findMonitorByClassId(classId);
		List<UserDTO> usersDTO = users.stream().map(userMapper::toDTO).collect(Collectors.toList());
		return usersDTO;
	}
}
