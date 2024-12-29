package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.dto.AttendanceDTO;
import com.rafael.atendimento.entity.Attendance;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.service.CustomerServiceService;
import com.rafael.atendimento.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AttendanceMapper {
	
	private final CustomerServiceService customerService;
	private final UserService userService;
	
	public AttendanceDTO toDTO(Attendance attendance) {
	    if (attendance == null) {
	        return null;
	    }
	    return new AttendanceDTO(
	    	attendance.getId(),
	    	attendance.getCustomerService().getId(),
	    	attendance.getUser().getId(),
	    	attendance.getUser().getName(),
	    	attendance.getStatus().getValue(),
	    	attendance.getDate(),
	    	attendance.getTime()
	    );
	}
	
	public Attendance toEntity(AttendanceDTO attendanceDTO) {
	    if (attendanceDTO == null) {
	        return null;
	    }

	    Attendance attendance = new Attendance();
	    if (attendanceDTO.id() != null) {
	    	attendance.setId(attendanceDTO.id());
	    }
	    attendance.setCustomerService(customerService.findServiceById(attendanceDTO.customerServiceId()));
	    attendance.setUser(userService.findUserById(attendanceDTO.userId()));
	    attendance.setStatus(convertServiceStatusValue(attendanceDTO.status()));
	    attendance.setDate(attendanceDTO.date());
	    attendance.setTime(attendanceDTO.time());
	    return attendance;
	}
	
	public AttendanceStatus convertServiceStatusValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "Presente" -> AttendanceStatus.PRESENTE;
            case "Ausente" -> AttendanceStatus.AUSENTE;
            default -> throw new IllegalArgumentException("Categoria inválida: " + value);
        };
    }
}
