package com.rafael.atendimento.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rafael.atendimento.dto.ScheduleDTO;
import com.rafael.atendimento.entity.Calendar;
import com.rafael.atendimento.entity.Schedule;
import com.rafael.atendimento.repository.CalendarRepository;
import com.rafael.atendimento.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	
	private final CalendarRepository calendarRepository;
    private final ScheduleRepository scheduleRepository;
    
 // Adicionar um horário ao calendário
    public ScheduleDTO addSchedule(Long calendarId, ScheduleDTO scheduleDTO) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));

        Schedule schedule = new Schedule();
        schedule.setCalendar(calendar);
        schedule.setDayOfWeek(scheduleDTO.dayOfWeek());
        schedule.setStartTime(scheduleDTO.startTime());
        schedule.setEndTime(scheduleDTO.endTime());

        scheduleRepository.save(schedule);

        return new ScheduleDTO(
                schedule.getId(),
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }

    // Listar horários de um calendário
    public List<ScheduleDTO> getSchedulesByCalendarId(Long calendarId) {
        List<Schedule> schedules = scheduleRepository.findByCalendarId(calendarId);
        return schedules.stream()
                .map(schedule -> new ScheduleDTO(
                        schedule.getId(),
                        schedule.getDayOfWeek(),
                        schedule.getStartTime(),
                        schedule.getEndTime()
                )).toList();
    }

    // Remover um horário do calendário
    public void deleteSchedule(Long calendarId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findByIdAndCalendarId(scheduleId, calendarId)
                .orElseThrow(() -> new RuntimeException("Shedule not found"));
        scheduleRepository.delete(schedule);
    }
}
