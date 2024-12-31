package com.rafael.atendimento.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rafael.atendimento.dto.AttendanceDTO;
import com.rafael.atendimento.dto.CustomerServiceDTO;
import com.rafael.atendimento.dto.mapper.AttendanceMapper;
import com.rafael.atendimento.entity.Attendance;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.repository.AttendanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
	private final CustomerServiceService customerService;
	private final UserService userService;
	private final AttendanceMapper attendanceMapper;
	
	// Busca presença por atendimento e usuário
    public Optional<Attendance> findAttendanceByUserAndService(Long customerServiceId, Long userId) {
        return attendanceRepository.findByCustomerServiceIdAndUserId(customerServiceId, userId);
    }
    
    // Busca todas as presenças de um atendimento
    public List<AttendanceDTO> findAttendancesByCustomerService(Long customerServiceId) {
        return attendanceRepository.findByCustomerServiceId(customerServiceId)
        		.stream()
        		.map(attendanceMapper::toDTO)
        		.collect(Collectors.toList());
    }
	
	public AttendanceDTO recordAttendance(Long serviceId, Long userId, AttendanceStatus status) {
		
		Optional<Attendance> existingAttendance = findAttendanceByUserAndService(serviceId, userId);
		if (existingAttendance.isPresent()) {
            throw new IllegalArgumentException("Presença já registrada para este usuário neste atendimento.");
        }
		
		User user = userService.findUserById(userId);
		CustomerService service = customerService.findServiceById(serviceId);
		
		if(service.getOwner() != user && service.getStudent() != user) {
			throw new IllegalArgumentException("Usuário não pertence ao atendimento.");
		}
		
		// Valida se a data e horário são válidos
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDateTime = LocalDateTime.of(service.getDate(), service.getTime_start());

		if (now.isBefore(startDateTime)) {
		    throw new IllegalArgumentException("Registro de presença não permitido fora do horário do atendimento.");
		}
		
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setCustomerService(service);
        attendance.setStatus(status);
        attendance.setDate(LocalDate.now());
        attendance.setTime(LocalTime.now());

        Attendance saved = attendanceRepository.save(attendance);
        
        updateStatusOfService(service);

        if (status == AttendanceStatus.AUSENTE) {
            userService.verificarSuspensao(userId);
        }
        
        return attendanceMapper.toDTO(saved);
    }
	
	private void updateStatusOfService(CustomerService service) {
		service.atualizarStatus();
        customerService.update(service.getId(), new CustomerServiceDTO(service.getId(), 
        		service.getTitle(), service.getDescription(), service.getDate(), service.getTime_start(), 
        			service.getTime_end(), service.getStatus().toString(), service.getClazz().getId(), service.getOwner().getId(), 
        				service.getStudent().getId()));
	}
	
	public AttendanceDTO updateAttendance(Long attendanceId, AttendanceStatus newStatus) {
        // Busca o registro de presença pelo ID
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Registro de presença não encontrado."));

        // Atualiza os campos desejados
        if (newStatus != null) {
            attendance.setStatus(newStatus);
        }

        // Salva as alterações
        Attendance saved = attendanceRepository.save(attendance);
        
        updateStatusOfService(saved.getCustomerService());
        
        return attendanceMapper.toDTO(saved);
    }
}
