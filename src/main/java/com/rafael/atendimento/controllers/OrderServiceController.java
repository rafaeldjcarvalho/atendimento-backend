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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.OrderServiceDTO;
import com.rafael.atendimento.service.OrderServiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderServiceController {
	
	private final OrderServiceService orderService;
	
	// Buscar todos os pedidos de atendimento
    @GetMapping
    public ResponseEntity<List<OrderServiceDTO>> getAllOrders() {
        List<OrderServiceDTO> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    // Buscar pedido de atendimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderServiceDTO> getOrderById(@PathVariable Long id) {
        OrderServiceDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }
    
    // Criar um novo pedido de atendimento
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderServiceDTO order) {
    	try {
    		OrderServiceDTO createdOrder = orderService.create(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    	} catch(RuntimeException ex) {
    		return ResponseEntity.badRequest().body(ex.getMessage());
    	}
    }
    
    // Atualizar pedido de atendimento por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderServiceDTO order) {
        try {
        	OrderServiceDTO updatedOrder = orderService.update(id, order);
            return ResponseEntity.ok(updatedOrder);
        } catch(RuntimeException ex) {
        	return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    // Excluir pedido de atendimento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Buscar pedidos por turma
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<OrderServiceDTO>> getOrdersByClass(@PathVariable Long classId) {
        List<OrderServiceDTO> orders = orderService.getOrdersByClass(classId);
        return ResponseEntity.ok(orders);
    }
    
    // Buscar pedidos por usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderServiceDTO>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderServiceDTO> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

}
