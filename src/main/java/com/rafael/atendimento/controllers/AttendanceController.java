package com.rafael.atendimento.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.dto.AttendanceDTO;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.exception.AccessDeniedException;
import com.rafael.atendimento.exception.AccountInactiveException;
import com.rafael.atendimento.exception.InvalidTokenException;
import com.rafael.atendimento.infra.security.TokenService;
import com.rafael.atendimento.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	private final TokenService tokenService;
	
	@GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendancesByService(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long serviceId) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
        return ResponseEntity.ok(attendanceService.findAttendancesByCustomerService(serviceId));
    }
	
	@PostMapping("/{customerServiceId}/register")
    public ResponseEntity<?> registerAttendance(
    		@RequestHeader("Authorization") String token,
            @PathVariable Long customerServiceId,
            @RequestParam Long userId,
            @RequestParam AttendanceStatus status) {
        try {
        	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), false);
            AttendanceDTO created = attendanceService.recordAttendance(customerServiceId, userId, status);
            return ResponseEntity.ok(created);
        } catch (AccessDeniedException | AccountInactiveException | InvalidTokenException ex) {
            throw ex;
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
	
	@PutMapping("/update/{attendanceId}")
    public ResponseEntity<AttendanceDTO> updateAttendance(
    		@RequestHeader("Authorization") String token,
            @PathVariable Long attendanceId,
            @RequestParam AttendanceStatus status) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), false);
        AttendanceDTO updatedAttendance = attendanceService.updateAttendance(attendanceId, status);
        return ResponseEntity.ok(updatedAttendance);
    }
}
