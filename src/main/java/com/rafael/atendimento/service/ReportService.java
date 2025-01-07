package com.rafael.atendimento.service;

import java.time.Duration;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rafael.atendimento.dto.ReportDTO;
import com.rafael.atendimento.dto.ServiceDayDTO;
import com.rafael.atendimento.dto.WeeklyHoursDTO;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.enums.ServiceStatus;
import com.rafael.atendimento.repository.CustomerServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	
	private final CustomerServiceRepository serviceRepository;
	
	public ReportDTO generateReport(Long classId) {
	    List<CustomerService> customerService = serviceRepository.findByClazz_Id(classId);

	    long total = customerService.size();
	    long completed = customerService.stream().filter(a -> a.getStatus() == ServiceStatus.CONCLUIDO).count();
	    long cancelled = customerService.stream().filter(a -> a.getStatus() == ServiceStatus.CANCELADO).count();
	    
	    List<ServiceDayDTO> serviceDays = customerService.stream()
	    	    .collect(Collectors.groupingBy(
	    	        CustomerService::getDate,
	    	        Collectors.summingDouble(service -> {
	    	            long duration = Duration.between(service.getTime_start(), service.getTime_end()).toMinutes();
	    	            return duration / 60.0; // Convertendo minutos para horas
	    	        })
	    	    ))
	    	    .entrySet()
	    	    .stream()
	    	    .map(entry -> new ServiceDayDTO(entry.getKey(), entry.getValue()))
	    	    .collect(Collectors.toList());
	    
	    Map<String, WeeklyHoursDTO> weeklyUsage = customerService.stream()
	    	    .collect(Collectors.groupingBy(
	    	        service -> service.getOwner().getName(), // Agrupa pelo nome do proprietário
	    	        Collectors.groupingBy(
	    	            service -> "Semana " + service.getDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR), // Nomeia as semanas
	    	            Collectors.summingDouble(service -> {
	    	                long duration = Duration.between(service.getTime_start(), service.getTime_end()).toMinutes();
	    	                return duration / 60.0; // Converte minutos para horas
	    	            })
	    	        )
	    	    ))
	    	    .entrySet()
	    	    .stream()
	    	    .collect(Collectors.toMap(
	    	        Map.Entry::getKey,
	    	        entry -> new WeeklyHoursDTO(
	    	            entry.getKey(),
	    	            entry.getValue()
	    	        )
	    	    ));

	    return new ReportDTO(total, completed, cancelled, serviceDays, weeklyUsage);
	}

}
