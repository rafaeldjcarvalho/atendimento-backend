package com.rafael.atendimento.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.OrderPageDTO;
import com.rafael.atendimento.dto.OrderServiceDTO;
import com.rafael.atendimento.infra.security.TokenService;
import com.rafael.atendimento.service.OrderServiceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderServiceController {
	
	private final OrderServiceService orderService;
	private final TokenService tokenService;
	
	// Buscar todos os pedidos de atendimento
//    @GetMapping
//    public ResponseEntity<List<OrderServiceDTO>> getAllOrders() {
//        List<OrderServiceDTO> orders = orderService.findAll();
//        return ResponseEntity.ok(orders);
//    }
	
	@GetMapping
    public OrderPageDTO list(
    		@RequestHeader("Authorization") String token,
    		@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
        return orderService.findAllPages(page, pageSize);
    }

    // Buscar pedido de atendimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderServiceDTO> getOrderById(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
        OrderServiceDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }
    
    // Criar um novo pedido de atendimento
    @PostMapping
    public ResponseEntity<?> createOrder(
    		@RequestHeader("Authorization") String token,
    		@RequestBody @Valid OrderServiceDTO order) {
    	try {
    		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor"), false);
    		OrderServiceDTO createdOrder = orderService.create(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    	} catch(RuntimeException ex) {
    		return ResponseEntity.badRequest().body(ex.getMessage());
    	}
    }
    
    // Atualizar pedido de atendimento por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id, 
    		@RequestBody @Valid OrderServiceDTO order) {
        try {
        	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor"), false);
        	OrderServiceDTO updatedOrder = orderService.update(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch(RuntimeException ex) {
        	return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    // Excluir pedido de atendimento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), false);
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Buscar pedidos por turma
    @GetMapping("/class/{classId}")
    public ResponseEntity<OrderPageDTO> getOrdersByClass(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId,
    		@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
        OrderPageDTO orders = orderService.getOrdersByClass(classId, page, pageSize);
        return ResponseEntity.ok(orders);
    }
    
    // Buscar pedidos por usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<OrderPageDTO> getOrdersByUser(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long userId, 
    		@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
    	OrderPageDTO orders = orderService.getOrdersByUser(userId, page, pageSize);
        return ResponseEntity.ok(orders);
    }

}
