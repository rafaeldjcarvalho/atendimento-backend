package com.rafael.atendimento.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.atendimento.entity.Attendance;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.service.AttendanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	
	@GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<Attendance>> getAttendancesByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(attendanceService.findAttendancesByCustomerService(serviceId));
    }
	
	@PostMapping("/{customerServiceId}/register")
    public ResponseEntity<?> registerAttendance(
            @PathVariable Long customerServiceId,
            @RequestParam Long userId,
            @RequestParam AttendanceStatus status) {

        try {
            Attendance created = attendanceService.recordAttendance(customerServiceId, userId, status);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
	
	@PutMapping("/update/{attendanceId}")
    public ResponseEntity<Attendance> updateAttendance(
            @PathVariable Long attendanceId,
            @RequestParam AttendanceStatus status) {
        Attendance updatedAttendance = attendanceService.updateAttendance(attendanceId, status);
        return ResponseEntity.ok(updatedAttendance);
    }
}
