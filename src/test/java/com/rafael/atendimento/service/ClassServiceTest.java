package com.rafael.atendimento.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.dto.mapper.ClassMapper;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;
import com.rafael.atendimento.repository.ClassRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;

    @Mock
    private UserService userService;

    @Mock
    private ClassMapper classMapper;

    @Mock
    private NotificationService notificationService;

    private ClassService classService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        classService = new ClassService(classRepository, userService, classMapper, notificationService);
    }

    @Test
    void testCreateClassAlreadyExists() {
        // Criando DTO de entrada
        UserDTO userDTO = new UserDTO(1L, "Professor", "owner@mail.com", "PROFESSOR", "ACTIVE");
        ClassDTO classDTO = new ClassDTO(null, "Math", LocalDate.now(), userDTO);

        // Mockando o comportamento
        when(classRepository.findByName(classDTO.name())).thenReturn(Optional.of(new com.rafael.atendimento.entity.Class()));

        // Executando o método e verificando exceção
        Exception exception = assertThrows(RuntimeException.class, () -> {
            classService.create(classDTO);
        });

        assertEquals("Class already exists", exception.getMessage());
    }

    @Test
    void testFindByIdClassNotFound() {
        // Mockando o comportamento
        when(classRepository.findById(1L)).thenReturn(Optional.empty());

        // Executando o método e verificando exceção
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            classService.findById(1L);
        });

        assertEquals("404 NOT_FOUND \"Class not found\"", exception.getMessage());
    }

    @Test
    void testDeleteClass() {
        // Criando classe existente
        com.rafael.atendimento.entity.Class existingClass = new com.rafael.atendimento.entity.Class();
        when(classRepository.findById(1L)).thenReturn(Optional.of(existingClass));

        // Executando o método
        classService.delete(1L);

        // Verificando a exclusão
        verify(classRepository).delete(existingClass);
    }

}
