package com.rafael.atendimento.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.OrderPageDTO;
import com.rafael.atendimento.dto.OrderServiceDTO;
import com.rafael.atendimento.dto.mapper.OrderMapper;
import com.rafael.atendimento.entity.OrderService;
import com.rafael.atendimento.entity.User;
import com.rafael.atendimento.entity.Class;
import com.rafael.atendimento.repository.OrderServiceRepository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceService {
	
	private final OrderServiceRepository orderRepository;
	private final OrderMapper orderMapper;
	private final ClassService classService;
	private final UserService userService;
	
//	public List<OrderServiceDTO> findAll() {
//		return orderRepository.findAll().stream()
//				.map(orderMapper::toDTO)
//				.collect(Collectors.toList());
//	}
	
	public OrderPageDTO findAllPages(@PositiveOrZero int page, @Positive @Max(100) int pageSize) {
        Page<OrderService> pageOrder = orderRepository.findAll(PageRequest.of(page, pageSize));
        List<OrderServiceDTO> orders = pageOrder.get().map(orderMapper::toDTO).collect(Collectors.toList());
        return new OrderPageDTO(orders, pageOrder.getTotalElements(), pageOrder.getTotalPages());
    }
	
	public OrderServiceDTO findById(Long id) {
		OrderService order = orderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
		return orderMapper.toDTO(order);
	}
	
	public OrderServiceDTO create(OrderServiceDTO orderDTO) {
		OrderService order = orderMapper.toEntity(orderDTO);
		Class clazz = classService.findClassById(orderDTO.classId());
		order.setClazz(clazz);
		User owner = userService.findUserById(orderDTO.userId());
		order.setOwner(owner);
		
		OrderService saved = orderRepository.save(order);
		
		return orderMapper.toDTO(saved);
	}
	
	public OrderServiceDTO update(Long id, OrderServiceDTO orderDTO) {
		OrderService order = orderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));
		order.setTitle(orderDTO.title());
		order.setDescription(orderDTO.description());
		order.setDate(orderDTO.date());
		order.setTime_start(orderDTO.time_start());
		order.setTime_end(orderDTO.time_end());
		order.setStatus(orderMapper.convertOrderStatusValue(orderDTO.status()));
		
		OrderService saved = orderRepository.save(order);
		
		return orderMapper.toDTO(saved);
	}
	
	public void delete(Long id) {
		orderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));
		orderRepository.deleteById(id);
	}
	
	// Relacionamentos
	public OrderPageDTO getOrdersByClass(
			Long classId, 
			@PositiveOrZero int page, 
			@Positive @Max(100) int pageSize) {
        Page<OrderService> pageOrder = orderRepository.findByClazz_Id(classId, PageRequest.of(page, pageSize));
        List<OrderServiceDTO> orders = pageOrder.get().map(orderMapper::toDTO).collect(Collectors.toList());
        return new OrderPageDTO(orders, pageOrder.getTotalElements(), pageOrder.getTotalPages());
    }

    public OrderPageDTO getOrdersByUser(
    		Long userId,
    		@PositiveOrZero int page, 
			@Positive @Max(100) int pageSize) {
    	Page<OrderService> pageOrder = orderRepository.findByOwner_Id(userId, PageRequest.of(page, pageSize));
        List<OrderServiceDTO> orders = pageOrder.get().map(orderMapper::toDTO).collect(Collectors.toList());
        return new OrderPageDTO(orders, pageOrder.getTotalElements(), pageOrder.getTotalPages());
    }

}
