package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.dto.CustomerServiceDTO;
import com.rafael.atendimento.entity.CustomerService;
import com.rafael.atendimento.enums.ServiceStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerServiceMapper {
	
	public CustomerServiceDTO toDTO(CustomerService service) {
	    if (service == null) {
	        return null;
	    }
	    return new CustomerServiceDTO(
	    	service.getId(),
	    	service.getTitle(),
	    	service.getDescription(),
	    	service.getDate(),
	    	service.getTime_start(),
	    	service.getTime_end(),
	    	service.getStatus().getValue(),
	    	service.getClazz().getId(),
	    	service.getOwner().getId(),
	    	service.getStudent().getId()
	    );
	}
	
	public CustomerService toEntity(CustomerServiceDTO serviceDTO) {
	    if (serviceDTO == null) {
	        return null;
	    }

	    CustomerService service = new CustomerService();
	    if (serviceDTO.id() != null) {
	    	service.setId(serviceDTO.id());
	    }
	    service.setTitle(serviceDTO.title());
	    service.setDescription(serviceDTO.description());
	    service.setDate(serviceDTO.date()); 
	    service.setTime_start(serviceDTO.time_start());
	    service.setTime_end(serviceDTO.time_end());
	    service.setStatus(convertServiceStatusValue(serviceDTO.status()));
	    service.setClazz(null);
	    service.setOwner(null);
	    service.setStudent(null);
	    return service;
	}
	
	public ServiceStatus convertServiceStatusValue(String value) {
        if (value == null) {
            return null;
        }
        return switch (value) {
            case "Pendente" -> ServiceStatus.PENDENTE;
            case "Concluido" -> ServiceStatus.CONCLUIDO;
            case "Cancelado" -> ServiceStatus.CANCELADO;
            default -> throw new IllegalArgumentException("Categoria inválida: " + value);
        };
    }

}
