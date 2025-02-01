package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rafael.atendimento.dto.CalendarDTO;
import com.rafael.atendimento.entity.Calendar;
import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.repository.CalendarRepository;
import com.rafael.atendimento.repository.ClassRepository;
import com.rafael.atendimento.repository.UserRepository;


class CalendarServiceTest {

    @InjectMocks
    private CalendarService calendarService;

    @Mock
    private ClassRepository classRepository;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerServiceService customerService;

    private Class clazz;
    private User owner;
    private Calendar calendar;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        owner = new User();
        owner.setId(1L);
        owner.setName("Professor");

        clazz = new Class();
        clazz.setId(1L);
        clazz.setMonitores(new ArrayList<>()); // Inicializa a lista
        clazz.setProfessores(new ArrayList<>()); // Inicializa a lista
        clazz.getProfessores().add(owner); // Adiciona o owner como professor

        calendar = new Calendar();
        calendar.setId(1L);
        calendar.setOwner(owner);
        calendar.setClazz(clazz);
        calendar.setSchedules(Collections.emptyList());
    }

    @Test
    void testCreateCalendar_Success() {
        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(calendarRepository.save(any())).thenReturn(calendar);

        CalendarDTO calendarDTO = new CalendarDTO(null, owner.getName(), owner.getId(), Collections.emptyList());

        CalendarDTO result = calendarService.createCalendar(1L, calendarDTO);

        assertNotNull(result);
        assertEquals(1L, result.ownerId());
    }

    @Test
    void testCreateCalendar_ClassNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());

        CalendarDTO calendarDTO = new CalendarDTO(null, owner.getName(), owner.getId(), Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () ->
                calendarService.createCalendar(1L, calendarDTO));

        assertEquals("Class not found", exception.getMessage());
    }

    @Test
    void testCreateCalendar_UserNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CalendarDTO calendarDTO = new CalendarDTO(null, owner.getName(), owner.getId(), Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () ->
                calendarService.createCalendar(1L, calendarDTO));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateCalendar_UserNotAuthorized() {
        User unauthorizedUser = new User();
        unauthorizedUser.setId(2L);
        when(classRepository.findById(1L)).thenReturn(Optional.of(clazz));
        when(userRepository.findById(2L)).thenReturn(Optional.of(unauthorizedUser));

        CalendarDTO calendarDTO = new CalendarDTO(null, unauthorizedUser.getName(), unauthorizedUser.getId(), Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () ->
                calendarService.createCalendar(1L, calendarDTO));

        assertEquals("Phroibed access", exception.getMessage());
    }

    @Test
    void testGetCalendarsByClassId_Success() {
        when(calendarRepository.findByClazzId(1L)).thenReturn(List.of(calendar));

        List<CalendarDTO> result = calendarService.getCalendarsByClassId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetCalendarsByClassId_Empty() {
        when(calendarRepository.findByClazzId(1L)).thenReturn(Collections.emptyList());

        List<CalendarDTO> result = calendarService.getCalendarsByClassId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteCalendar_Success() {
        when(calendarRepository.findByIdAndClazzId(1L, 1L)).thenReturn(Optional.of(calendar));
        when(customerService.findAllByOwner(1L)).thenReturn(Collections.emptyList());

        calendarService.deleteCalendar(1L, 1L);

        verify(calendarRepository, times(1)).delete(calendar);
    }

    @Test
    void testDeleteCalendar_NotFound() {
        when(calendarRepository.findByIdAndClazzId(1L, 1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                calendarService.deleteCalendar(1L, 1L));

        assertEquals("Calendar not found", exception.getMessage());
    }
}
