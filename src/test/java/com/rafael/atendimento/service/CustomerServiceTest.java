package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.rafael.atendimento.dto.CustomerServiceDTO;
import com.rafael.atendimento.dto.mapper.CustomerServiceMapper;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.repository.CustomerServiceRepository;

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

    @Test
    void testCreateCustomerService() {
        // Dados de entrada
        CustomerServiceDTO serviceDTO = new CustomerServiceDTO(
            1L, "Service Title", "Service Description", LocalDate.now(),
            LocalTime.of(10, 0), LocalTime.of(11, 0), "PENDING", 1L, 2L, 3L
        );

        // Mocking
        User owner = mock(User.class);
        User student = mock(User.class);
        com.rafael.atendimento.entity.Class clazz = mock(com.rafael.atendimento.entity.Class.class);
        CustomerService service = mock(CustomerService.class);

        when(serviceMapper.toEntity(serviceDTO)).thenReturn(service);
        when(classService.findClassById(1L)).thenReturn(clazz);
        when(userService.findUserById(2L)).thenReturn(owner);
        when(userService.findUserById(3L)).thenReturn(student);

        // Execução
        CustomerServiceDTO result = customerServiceService.create(serviceDTO);

        // Verificação
        assertNotNull(result);
        verify(serviceRepository).save(service);
    }
}
