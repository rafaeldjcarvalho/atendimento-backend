package com.rafael.atendimento.dto.mapper;

import org.springframework.stereotype.Component;

import com.rafael.atendimento.dto.OrderServiceDTO;
import com.rafael.atendimento.entity.OrderService;
import com.rafael.atendimento.enums.ServiceStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {
	
	public OrderServiceDTO toDTO(OrderService order) {
	    if (order == null) {
	        return null;
	    }
	    return new OrderServiceDTO(
	    	order.getId(),
	    	order.getTitle(),
	    	order.getDescription(),
	    	order.getDate(),
	    	order.getTime_start(),
	    	order.getTime_end(),
	    	order.getStatus().getValue(),
	    	order.getClazz().getId(),
	    	order.getOwner().getId()
	    );
	}
	
	public OrderService toEntity(OrderServiceDTO orderDTO) {
	    if (orderDTO == null) {
	        return null;
	    }

	    OrderService order = new OrderService();
	    if (orderDTO.id() != null) {
	    	order.setId(orderDTO.id());
	    }
	    order.setTitle(orderDTO.title());
	    order.setDescription(orderDTO.description());
	    order.setDate(orderDTO.date()); 
	    order.setTime_start(orderDTO.time_start());
	    order.setTime_end(orderDTO.time_end());
	    order.setStatus(convertOrderStatusValue(orderDTO.status()));
	    order.setClazz(null);
	    order.setOwner(null);
	    return order;
	}
	
	public ServiceStatus convertOrderStatusValue(String value) {
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
