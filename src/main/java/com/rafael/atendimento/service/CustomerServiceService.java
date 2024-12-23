package com.rafael.atendimento.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.CustomerPageDTO;
import com.rafael.atendimento.dto.CustomerServiceDTO;
import com.rafael.atendimento.dto.mapper.CustomerServiceMapper;
import com.rafael.atendimento.entity.Calendar;
import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.entity.Schedule;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.enums.DaysOfWeek;
import com.rafael.atendimento.repository.CustomerServiceRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceService {
	
	private final CustomerServiceRepository serviceRepository;
	private final CustomerServiceMapper serviceMapper;
	private final ClassService classService;
	private final UserService userService;
	
//	public List<CustomerServiceDTO> findAll() {
//		return serviceRepository.findAll().stream()
//				.map(serviceMapper::toDTO)
//				.collect(Collectors.toList());
//	}
	
	public CustomerPageDTO findAllPages(@PositiveOrZero int page, @Positive @Max(100) int pageSize) {
        Page<CustomerService> pageCustomer = serviceRepository.findAll(PageRequest.of(page, pageSize));
        List<CustomerServiceDTO> customers = pageCustomer.get().map(serviceMapper::toDTO).collect(Collectors.toList());
        return new CustomerPageDTO(customers, pageCustomer.getTotalElements(), pageCustomer.getTotalPages());
    }
	
	public CustomerServiceDTO findById(Long id) {
		CustomerService order = serviceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Service not found"));
		return serviceMapper.toDTO(order);
	}
	
	public CustomerServiceDTO create(CustomerServiceDTO serviceDTO) {
		CustomerService service = serviceMapper.toEntity(serviceDTO);
		Class clazz = classService.findClassById(serviceDTO.classId());
		service.setClazz(clazz);
		User owner = userService.findUserById(serviceDTO.userId());
		service.setOwner(owner);
		User student = userService.findUserById(serviceDTO.studentId());
		service.setStudent(student);
		
		validarCompatibilidadeDeHorarios(service, owner, clazz);
		
		CustomerService saved = serviceRepository.save(service);
		
		return serviceMapper.toDTO(saved);
	}
	
	public CustomerServiceDTO update(Long id, CustomerServiceDTO serviceDTO) {
		CustomerService service = serviceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Service not found"));
		service.setTitle(serviceDTO.title());
		service.setDescription(serviceDTO.description());
		service.setDate(serviceDTO.date());
		service.setTime_start(serviceDTO.time_start());
		service.setTime_end(serviceDTO.time_end());
		service.setStatus(serviceMapper.convertServiceStatusValue(serviceDTO.status()));
		
		User owner = service.getOwner();
		Class clazz = service.getClazz();
		
		validarCompatibilidadeDeHorarios(service, owner, clazz);
		
		CustomerService saved = serviceRepository.save(service);
		
		return serviceMapper.toDTO(saved);
	}
	
	public void delete(Long id) {
		serviceRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Service not found"));
		serviceRepository.deleteById(id);
	}
	
	// Métodos de relacionamentos
	
	public CustomerPageDTO getCustomerServicesByClass(
			Long classId,
			@PositiveOrZero int page, 
			@Positive @Max(100) int pageSize) {
		Page<CustomerService> pageCustomer = serviceRepository.findByClazz_Id(classId, PageRequest.of(page, pageSize));
        List<CustomerServiceDTO> customers = pageCustomer.get().map(serviceMapper::toDTO).collect(Collectors.toList());
        return new CustomerPageDTO(customers, pageCustomer.getTotalElements(), pageCustomer.getTotalPages());
    }

    public CustomerPageDTO getCustomerServicesByOwner(
    		Long ownerId,
    		@PositiveOrZero int page, 
			@Positive @Max(100) int pageSize) {
    	Page<CustomerService> pageCustomer = serviceRepository.findByOwner_Id(ownerId, PageRequest.of(page, pageSize));
        List<CustomerServiceDTO> customers = pageCustomer.get().map(serviceMapper::toDTO).collect(Collectors.toList());
        return new CustomerPageDTO(customers, pageCustomer.getTotalElements(), pageCustomer.getTotalPages());
    }

    public CustomerPageDTO getCustomerServicesByStudent(
    		Long studentId,
    		@PositiveOrZero int page, 
			@Positive @Max(100) int pageSize) {
    	Page<CustomerService> pageCustomer = serviceRepository.findByStudent_Id(studentId, PageRequest.of(page, pageSize));
        List<CustomerServiceDTO> customers = pageCustomer.get().map(serviceMapper::toDTO).collect(Collectors.toList());
        return new CustomerPageDTO(customers, pageCustomer.getTotalElements(), pageCustomer.getTotalPages());
    }
    
    private void validarCompatibilidadeDeHorarios(CustomerService service, User owner, Class clazz) {
        // Filtra o calendário do proprietário
        Calendar ownerCalendar = clazz.getCalendars().stream()
            .filter(calendar -> calendar.getOwner().getId().equals(owner.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("O proprietário não possui um calendário associado a esta turma."));

        // Verifica se o atendimento é compatível com os horários no calendário
        boolean isCompatible = ownerCalendar.getSchedules().stream().anyMatch(schedule -> 
            isTimeWithinRange(service.getDate(), service.getTime_start(), service.getTime_end(), schedule)
        );

        if (!isCompatible) {
            throw new IllegalArgumentException("Os horários do atendimento não são compatíveis com o calendário do proprietário.");
        }
    }
    
    private boolean isTimeWithinRange(LocalDate date, LocalTime start, LocalTime end, Schedule schedule) {
    	// Obtém o dia da semana em português
    	String dayOfWeekInPortuguese = DaysOfWeek.fromDayOfWeek(date.getDayOfWeek()).name();
    	
    	// Verifica se o dia da semana corresponde
        if (!schedule.getDayOfWeek().equalsIgnoreCase(dayOfWeekInPortuguese)) {
            return false;
        }
        
        return !start.isBefore(schedule.getStartTime()) && !end.isAfter(schedule.getEndTime());
    }
    
}
