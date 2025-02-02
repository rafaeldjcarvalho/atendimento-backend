package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rafael.atendimento.dto.ScheduleDTO;
import com.rafael.atendimento.entity.Calendar;
import com.rafael.atendimento.entity.Schedule;
import com.rafael.atendimento.repository.CalendarRepository;
import com.rafael.atendimento.repository.ScheduleRepository;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private CalendarRepository calendarRepository;
    
    @Mock
    private ScheduleRepository scheduleRepository;
    
    @InjectMocks
    private ScheduleService scheduleService;
    
    private Calendar calendar;
    private Schedule schedule;
    private ScheduleDTO scheduleDTO;
    
    @BeforeEach
    void setUp() {
        calendar = new Calendar();
        calendar.setId(1L);
        
        schedule = new Schedule();
        schedule.setId(1L);
        schedule.setDayOfWeek("MONDAY");
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));
        schedule.setCalendar(calendar);
        
        scheduleDTO = new ScheduleDTO(
            1L, "MONDAY", LocalTime.of(9, 0), LocalTime.of(10, 0)
        );
    }
    
    @Test
    void testAddSchedule_Success() {
        when(calendarRepository.findById(1L)).thenReturn(Optional.of(calendar));
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);
        
        ScheduleDTO result = scheduleService.addSchedule(1L, scheduleDTO);
        
        assertNotNull(result);
        assertEquals("MONDAY", result.dayOfWeek());
        assertEquals(LocalTime.of(9, 0), result.startTime());
        assertEquals(LocalTime.of(10, 0), result.endTime());
    }
    
    @Test
    void testAddSchedule_CalendarNotFound() {
        when(calendarRepository.findById(1L)).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            scheduleService.addSchedule(1L, scheduleDTO)
        );
        
        assertEquals("Calendar not found", exception.getMessage());
    }
    
    @Test
    void testGetSchedulesByCalendarId_Success() {
        when(scheduleRepository.findByCalendarId(1L)).thenReturn(List.of(schedule));
        
        List<ScheduleDTO> result = scheduleService.getSchedulesByCalendarId(1L);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("MONDAY", result.get(0).dayOfWeek());
    }
    
    @Test
    void testDeleteSchedule_Success() {
        when(scheduleRepository.findByIdAndCalendarId(1L, 1L)).thenReturn(Optional.of(schedule));
        
        assertDoesNotThrow(() -> scheduleService.deleteSchedule(1L, 1L));
        verify(scheduleRepository, times(1)).delete(schedule);
    }
    
    @Test
    void testDeleteSchedule_NotFound() {
        when(scheduleRepository.findByIdAndCalendarId(1L, 1L)).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            scheduleService.deleteSchedule(1L, 1L)
        );
        
        assertEquals("Shedule not found", exception.getMessage());
    }
}
