package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafael.atendimento.dto.AttendanceDTO;
import com.rafael.atendimento.dto.mapper.AttendanceMapper;
import com.rafael.atendimento.entity.Attendance;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.repository.AttendanceRepository;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {
    
    @Mock
    private AttendanceRepository attendanceRepository;
    
    @Mock
    private CustomerServiceService customerService;
    
    @Mock
    private UserService userService;
    
    @Mock
    private AttendanceMapper attendanceMapper;
    
    @InjectMocks
    private AttendanceService attendanceService;
    
    private Attendance attendance;
    private User user;
    private CustomerService service;
    
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        service = new CustomerService();
        service.setId(1L);
        service.setOwner(user); // Defina o dono do atendimento
        service.setStudent(user); // Defina o aluno do atendimento

        attendance = new Attendance();
        attendance.setUser(user);
        attendance.setCustomerService(service);
        attendance.setStatus(AttendanceStatus.PRESENTE);
    }
    
    @Test
    void testFindAttendanceByUserAndService() {
        when(attendanceRepository.findByCustomerServiceIdAndUserId(1L, 1L)).thenReturn(Optional.of(attendance));
        Optional<Attendance> result = attendanceService.findAttendanceByUserAndService(1L, 1L);
        assertTrue(result.isPresent());
    }
    
    @Test
    void testFindAttendancesByCustomerService() {
        when(attendanceRepository.findByCustomerServiceId(1L)).thenReturn(Arrays.asList(attendance));
        List<AttendanceDTO> result = attendanceService.findAttendancesByCustomerService(1L);
        assertNotNull(result);
    }
    
    @Test
    void testRecordAttendance_UserNotInService() {
        User anotherUser = new User();
        anotherUser.setId(2L);
        when(userService.findUserById(2L)).thenReturn(anotherUser);
        when(customerService.findServiceById(1L)).thenReturn(service);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            attendanceService.recordAttendance(1L, 2L, AttendanceStatus.PRESENTE);
        });
        
        assertEquals("Usuário não pertence ao atendimento.", exception.getMessage());
    }
    
    @Test
    void testRecordAttendance_AlreadyExists() {
        when(attendanceRepository.findByCustomerServiceIdAndUserId(1L, 1L)).thenReturn(Optional.of(attendance));
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            attendanceService.recordAttendance(1L, 1L, AttendanceStatus.PRESENTE);
        });
        
        assertEquals("Presença já registrada para este usuário neste atendimento.", exception.getMessage());
    }
    
    @Test
    void testUpdateAttendance_NotFound() {
        when(attendanceRepository.findById(2L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            attendanceService.updateAttendance(2L, AttendanceStatus.PRESENTE);
        });
        
        assertEquals("Registro de presença não encontrado.", exception.getMessage());
    }
}

