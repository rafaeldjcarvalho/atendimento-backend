package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafael.atendimento.repository.ClassRepository;
import com.rafael.atendimento.repository.UserRepository;
import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.UserDTO;
import com.rafael.atendimento.dto.mapper.ClassMapper;
import com.rafael.atendimento.entity.Class;

@ExtendWith(MockitoExtension.class)
class ClassServiceTest {

    @Mock
    private ClassRepository classRepository;
    
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClassService classService;
    
    @InjectMocks
	private UserService userService;
    
    @InjectMocks
	private ClassMapper classMapper;

    private Class clazz;

    @BeforeEach
    void setUp() {
        clazz = new Class();
        clazz.setId(1L);
        clazz.setName("Turma Teste");
    }

    @Test
    void testCreateClass_NameAlreadyExists() {
        when(classRepository.findByName("Turma Teste")).thenReturn(Optional.of(clazz));
        
        Exception exception = assertThrows(RuntimeException.class, () -> 
            classService.create(new ClassDTO(1L, "Turma Teste", LocalDate.now(), new UserDTO(1L, "paulo", "teste@gmail.com", "Professor", "Ativo")))
        );
        assertEquals("Class already exists", exception.getMessage());
    }

    @Test
    void testFindById_ClassNotFound() {
        when(classRepository.findById(2L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> 
            classService.findById(2L)
        );
        assertEquals("404 NOT_FOUND \"Class not found\"", exception.getMessage());
    }

    @Test
    void testListClasses_InvalidPagination() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> 
            classService.list(-1, 10)
        );
        assertEquals("Page index must not be less than zero", exception.getMessage());
    }

    @Test
    void testDeleteClass_Success() {
        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));
        doNothing().when(classRepository).delete(clazz);
        
        assertDoesNotThrow(() -> classService.delete(1L));
    }

    @Test
    void testDeleteClass_NotFound() {
        when(classRepository.findById(2L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> 
            classService.delete(2L)
        );
        assertEquals("404 NOT_FOUND \"Class not found\"", exception.getMessage());
    }
}
