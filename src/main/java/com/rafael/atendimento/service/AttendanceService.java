package com.rafael.atendimento.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rafael.atendimento.dto.UpdateRequestDTO;
import com.rafael.atendimento.entity.Attendance;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;
import com.rafael.atendimento.repository.AttendanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {
	
	private final AttendanceRepository attendanceRepository;
	private final CustomerServiceService customerService;
	private final UserService userService;
	
	// Busca presença por atendimento e usuário
    public Optional<Attendance> findAttendanceByUserAndService(Long customerServiceId, Long userId) {
        return attendanceRepository.findByCustomerServiceIdAndUserId(customerServiceId, userId);
    }
    
    // Busca todas as presenças de um atendimento
    public List<Attendance> findAttendancesByCustomerService(Long customerServiceId) {
        return attendanceRepository.findByCustomerServiceId(customerServiceId);
    }
	
	public Attendance recordAttendance(Long serviceId, Long userId, AttendanceStatus status) {
		
		Optional<Attendance> existingAttendance = findAttendanceByUserAndService(serviceId, userId);
		if (existingAttendance.isPresent()) {
            throw new IllegalArgumentException("Presença já registrada para este usuário neste atendimento.");
        }
		
		User user = userService.findUserById(userId);
		CustomerService service = customerService.findServiceById(serviceId);
		
		if(!(service.getOwner() == user) || !(service.getStudent() == user)) {
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

        if (status == AttendanceStatus.AUSENTE) {
            handleAbsence(userId, saved.getUser().getTypeAccess());
        }
        
        return saved;
    }
	
	private void handleAbsence(Long userId, TypeAccess role) {
        // Lógica para aplicar punição com base no tipo de usuário
        long absences = attendanceRepository.countByUserIdAndStatus(userId, AttendanceStatus.AUSENTE);
        if (absences >= 3) {
            User user = userService.findUserById(userId);
            user.setStatus(UserStatus.SUSPENSO); // Exemplo de punição
            userService.update(user.getId(), new UpdateRequestDTO(user.getName(), 
            		user.getEmail(), user.getPassword(), user.getTypeAccess().toString(), 
            			user.getStatus().toString()));
        }
    }
	
	public Attendance updateAttendance(Long attendanceId, AttendanceStatus newStatus) {
        // Busca o registro de presença pelo ID
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Registro de presença não encontrado."));

        // Atualiza os campos desejados
        if (newStatus != null) {
            attendance.setStatus(newStatus);
        }

        // Salva as alterações
        return attendanceRepository.save(attendance);
    }
}
