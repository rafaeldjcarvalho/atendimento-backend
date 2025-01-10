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

import com.rafael.atendimento.dto.CustomerPageDTO;
import com.rafael.atendimento.dto.CustomerServiceDTO;
import com.rafael.atendimento.infra.security.TokenService;
import com.rafael.atendimento.service.CustomerServiceService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customerService")
@RequiredArgsConstructor
public class CustomerServiceController {
	
	private final CustomerServiceService customerService;
	private final TokenService tokenService;
	
	// Buscar Todos
//	@GetMapping
//	public ResponseEntity<List<CustomerServiceDTO>> getAllServices() {
//		List<CustomerServiceDTO> orders = customerService.findAll();
//		return ResponseEntity.ok(orders);
//	}
	
	@GetMapping
    public CustomerPageDTO list(
    		@RequestHeader("Authorization") String token,
    		@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
        return customerService.findAllPages(page, pageSize);
    }
	
	// Buscar atendimento por ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerServiceDTO> getServiceById(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
    	CustomerServiceDTO order = customerService.findById(id);
        return ResponseEntity.ok(order);
    }
    
    // Criar um novo pedido de atendimento
    @PostMapping
    public ResponseEntity<?> createService(
    		@RequestHeader("Authorization") String token,
    		@RequestBody @Valid CustomerServiceDTO order) {
    	try {
    		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Monitor", "Professor"), false);
    		CustomerServiceDTO createdService = customerService.create(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
    	} catch(RuntimeException ex) {
    		return ResponseEntity.badRequest().body(ex.getMessage());
    	}
    }
    
    // Atualizar pedido de atendimento por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id, 
    		@RequestBody @Valid CustomerServiceDTO service) {
        try {
        	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Monitor", "Professor"), false);
        	CustomerServiceDTO updatedService = customerService.update(id, service);
            return ResponseEntity.ok(updatedService);
        } catch(RuntimeException ex) {
        	return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    // Excluir pedido de atendimento por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Monitor", "Professor"), false);
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Buscar apenas o da classe
    @GetMapping("/class/{id_class}")
	public CustomerPageDTO getAllServiceByClass(
			@RequestHeader("Authorization") String token,
			@PathVariable Long id_class, 
			@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		return customerService.getCustomerServicesByClass(id_class, page, pageSize);
	}
    
    // Buscar apenas do Professor/Monitor
    @GetMapping("/owner/{id_owner}")
	public CustomerPageDTO getAllServiceByOwner(
			@RequestHeader("Authorization") String token,
			@PathVariable Long id_owner,
			@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		return customerService.getCustomerServicesByOwner(id_owner, page, pageSize);
	}
    
    @GetMapping("/owner/services/{id_owner}")
	public List<CustomerServiceDTO> getAllServiceByOwner(
			@RequestHeader("Authorization") String token,
			@PathVariable Long id_owner) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		return customerService.findAllByOwner(id_owner);
	}
    
    // Buscar apenas do Aluno
    @GetMapping("/student/{id_student}")
	public CustomerPageDTO getAllServiceByStudent(
			@RequestHeader("Authorization") String token,
			@PathVariable Long id_student,
			@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		return customerService.getCustomerServicesByStudent(id_student, page, pageSize);
	}

}
