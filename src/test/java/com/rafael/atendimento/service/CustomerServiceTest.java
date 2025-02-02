package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.CustomerPageDTO;
import com.rafael.atendimento.dto.CustomerServiceDTO;
import com.rafael.atendimento.dto.mapper.CustomerServiceMapper;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.repository.CustomerServiceRepository;
import com.rafael.atendimento.entity.Class;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    @Mock
    private CustomerServiceRepository serviceRepository;
    
    @Mock
    private CustomerServiceMapper serviceMapper;
    
    @Mock
    private ClassService classService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private NotificationService notificationService;
    
    @InjectMocks
    private CustomerServiceService customerServiceService;
    
    private CustomerService service;
    private CustomerServiceDTO serviceDTO;
    private Class clazz;
    private User owner;
    private User student;
    
    @BeforeEach
    void setUp() {
        clazz = new Class();
        clazz.setId(1L);
        
        owner = new User();
        owner.setId(1L);
        
        student = new User();
        student.setId(2L);
        
        service = new CustomerService();
        service.setId(1L);
        service.setTitle("Atendimento de Matemática");
        service.setDescription("Ajuda com derivadas");
        service.setDate(LocalDate.now());
        service.setTime_start(LocalTime.of(10, 0));
        service.setTime_end(LocalTime.of(11, 0));
        service.setClazz(clazz);
        service.setOwner(owner);
        service.setStudent(student);
        
        serviceDTO = new CustomerServiceDTO(1L, "Atendimento de Matemática", "Ajuda com derivadas",
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), "PENDENTE", 1L, 1L, 2L);
    }
    
    @Test
    void testFindAllPages() {
        Page<CustomerService> page = new PageImpl<>(List.of(service));
        when(serviceRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(serviceMapper.toDTO(any())).thenReturn(serviceDTO);
        
        CustomerPageDTO result = customerServiceService.findAllPages(0, 10);
        
        assertNotNull(result);
        assertEquals(1, result.services().size());
        assertEquals(1, result.totalElementos());
        verify(serviceRepository).findAll(any(Pageable.class));
    }
    
    @Test
    void testFindAllByOwner() {
        when(serviceRepository.findByOwner_Id(anyLong())).thenReturn(List.of(service));
        when(serviceMapper.toDTO(any())).thenReturn(serviceDTO);
        
        List<CustomerServiceDTO> result = customerServiceService.findAllByOwner(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(serviceRepository).findByOwner_Id(1L);
    }
    
    @Test
    void testFindById_Success() {
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.of(service));
        when(serviceMapper.toDTO(any())).thenReturn(serviceDTO);
        
        CustomerServiceDTO result = customerServiceService.findById(1L);
        
        assertNotNull(result);
        assertEquals(serviceDTO, result);
        verify(serviceRepository).findById(1L);
    }
    
    @Test
    void testFindById_NotFound() {
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        assertThrows(ResponseStatusException.class, () -> customerServiceService.findById(99L));
        verify(serviceRepository).findById(99L);
    }
    
    @Test
    void testDelete() {
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.of(service));
        doNothing().when(serviceRepository).deleteById(anyLong());
        
        customerServiceService.delete(1L);
        
        verify(serviceRepository).deleteById(1L);
    }
}

