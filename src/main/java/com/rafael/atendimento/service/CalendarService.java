package com.rafael.atendimento.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rafael.atendimento.entity.Calendar;
import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.dto.CalendarDTO;
import com.rafael.atendimento.dto.ScheduleDTO;
import com.rafael.atendimento.repository.CalendarRepository;
import com.rafael.atendimento.repository.ClassRepository;
import com.rafael.atendimento.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarService {
	
	private final ClassRepository classRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    
 // Criar um calendário
    public CalendarDTO createCalendar(Long classId, CalendarDTO calendarDTO) {
        Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        User owner = userRepository.findById(calendarDTO.ownerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!clazz.getMonitores().contains(owner) && !clazz.getProfessores().contains(owner)) {
            throw new RuntimeException("Phroibed access");
        }

        Calendar calendar = new Calendar();
        calendar.setOwner(owner);
        calendar.setClazz(clazz);

        calendarRepository.save(calendar);

        return new CalendarDTO(calendar.getId(), owner.getName(), owner.getId(), new ArrayList<ScheduleDTO>());
    }
    
 // Listar calendários de uma turma
    public List<CalendarDTO> getCalendarsByClassId(Long classId) {
        List<Calendar> calendars = calendarRepository.findByClazzId(classId);
        return calendars.stream()
                .map(calendar -> new CalendarDTO(
                        calendar.getId(),
                        calendar.getOwner().getName(),
                        calendar.getOwner().getId(),
                        calendar.getSchedules().stream()
                                .map(schedule -> new ScheduleDTO(
                                        schedule.getId(),
                                        schedule.getDayOfWeek(),
                                        schedule.getStartTime(),
                                        schedule.getEndTime()
                                )).toList()))
                .toList();
    }
    
 // Deletar um calendário
    public void deleteCalendar(Long classId, Long calendarId) {
        Calendar calendar = calendarRepository.findByIdAndClazzId(calendarId, classId)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));
        calendarRepository.delete(calendar);
    }

}
