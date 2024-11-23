package com.rafael.atendimento.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.ScheduleDTO;
import com.rafael.atendimento.service.ScheduleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {
	
	//private final CalendarService calendarService;
    private final ScheduleService scheduleService;
    
 // Adicionar um horário ao calendário
    @PostMapping("/{calendarId}/schedules")
    public ResponseEntity<?> addSchedule(@PathVariable Long calendarId, @RequestBody @Valid ScheduleDTO scheduleDTO) {
        try {
            ScheduleDTO newSchedule = scheduleService.addSchedule(calendarId, scheduleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSchedule);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
 // Listar horários de um calendário
    @GetMapping("/{calendarId}/schedules")
    public ResponseEntity<List<ScheduleDTO>> listSchedules(@PathVariable Long calendarId) {
        List<ScheduleDTO> schedules = scheduleService.getSchedulesByCalendarId(calendarId);
        return ResponseEntity.ok(schedules);
    }
    
 // Remover um horário de um calendário
    @DeleteMapping("/{calendarId}/schedules/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long calendarId, @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(calendarId, scheduleId);
        return ResponseEntity.noContent().build();
    }

}
