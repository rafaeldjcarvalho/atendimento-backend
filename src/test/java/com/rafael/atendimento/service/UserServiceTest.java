package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.RegisterRequestDTO;
import com.rafael.atendimento.dto.UpdateRequestDTO;
import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.dto.mapper.UserMapper;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;
import com.rafael.atendimento.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John Doe", "john@example.com", "password", TypeAccess.ALUNO, UserStatus.ATIVO, null, null, null, null, null, null, null, null, null, null);
        userDTO = new UserDTO(1L, "John Doe", "john@example.com", "ALUNO", "ATIVO");
    }

    @Test
    void testFindAll_Success() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.findAll();
        
        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.findById(1L);
        
        assertEquals(userDTO, result);
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.findById(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }


    @Test
    void testCreate_UserAlreadyExists() {
        RegisterRequestDTO request = new RegisterRequestDTO("John Doe", "john@example.com", "password", "STUDENT");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.create(request));
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void testUpdate_NotFound() {
        UpdateRequestDTO request = new UpdateRequestDTO("John Updated", "john@example.com", "newpassword", "STUDENT", "ATIVO");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.update(1L, request));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDelete_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        assertDoesNotThrow(() -> userService.delete(1L));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDelete_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.delete(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}

