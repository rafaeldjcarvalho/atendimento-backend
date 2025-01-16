package com.rafael.atendimento.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
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

import com.itextpdf.text.DocumentException;
import com.rafael.atendimento.dto.CalendarDTO;
import com.rafael.atendimento.dto.ClassDTO;
import com.rafael.atendimento.dto.ClassPageDTO;
import com.rafael.atendimento.dto.ReportDTO;
import com.rafael.atendimento.infra.security.TokenService;
import com.rafael.atendimento.service.CalendarService;
import com.rafael.atendimento.service.ClassService;
import com.rafael.atendimento.service.ReportService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor
public class ClassController {
	
	private final ClassService classService;
	private final CalendarService calendarService;
	private final ReportService reportService;
	private final TokenService tokenService;
	
//	@GetMapping
//	public List<ClassDTO> findAll() {
//		return classService.findAll();
//	}
	
	@GetMapping
    public ClassPageDTO list(
    		@RequestHeader("Authorization") String token,
    		@RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "12") @Positive @Max(100) int pageSize) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
        return classService.list(page, pageSize);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<ClassDTO> findById(
			@RequestHeader("Authorization") String token,
			@PathVariable Long id) {
		ClassDTO findClass = classService.findById(id);
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		return ResponseEntity.ok(findClass);
	}
	
	@PostMapping
    public ResponseEntity<?> createClass(
    		@RequestHeader("Authorization") String token,
    		@RequestBody @Valid ClassDTO classRequest) {
	   tokenService.validateTokenAndPermissions(token, List.of("Admin", "Professor"), false);
	   ClassDTO findClass = classService.create(classRequest);
	   return ResponseEntity.ok(findClass);
    }
	
	@PutMapping("/{id}")
    public ResponseEntity<?> updateClass(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id, 
    		@RequestBody @Valid ClassDTO classRequest) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Professor"), false);
		ClassDTO findClass = classService.update(id, classRequest);
		return ResponseEntity.ok(findClass);
    }
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClass(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long id) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Professor"), false);
		classService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	// Métodos de Relacionamtentos
	
	@PostMapping("/{classId}/add-user/{userId}")
	public ResponseEntity<?> addUserInClass(
			@RequestHeader("Authorization") String token,
			@PathVariable Long classId, 
			@PathVariable Long userId) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), false);
		ClassDTO updatedClass = classService.addUserInClass(classId, userId);
		return ResponseEntity.ok(updatedClass);
	}
	
	@DeleteMapping("/{classId}/remove-user/{userId}")
	public ResponseEntity<?> removeUserInClass(
			@RequestHeader("Authorization") String token,
			@PathVariable Long classId, 
			@PathVariable Long userId) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), false);
		ClassDTO updatedClass = classService.deleteUserInClass(classId, userId);
		return ResponseEntity.ok(updatedClass);
	}
	
	@GetMapping("/class-user/{userId}")
    public ResponseEntity<?> getUserClasses(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long userId) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
		List<ClassDTO> classes = classService.getClassesForUser(userId);
        return ResponseEntity.ok(classes);
    }
	
	// Criar um novo calendário para a turma
	@PostMapping("/{classId}/calendars")
    public ResponseEntity<?> createCalendar(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId, 
    		@RequestBody @Valid CalendarDTO calendarDTO) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Monitor", "Professor"), false);
        CalendarDTO newCalendar = calendarService.createCalendar(classId, calendarDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCalendar);
    }
	
	// Listar todos os calendários de uma turma
    @GetMapping("/{classId}/calendars")
    public ResponseEntity<List<CalendarDTO>> listCalendars(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), true);
        List<CalendarDTO> calendars = calendarService.getCalendarsByClassId(classId);
        return ResponseEntity.ok(calendars);
    }
    
 // Deletar um calendário
    @DeleteMapping("/{classId}/calendars/{calendarId}")
    public ResponseEntity<Void> deleteCalendar(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId, 
    		@PathVariable Long calendarId) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Monitor", "Professor"), false);
        calendarService.deleteCalendar(classId, calendarId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{classId}/promote/{userId}")
    public ResponseEntity<?> promoteToMonitor(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId, 
    		@PathVariable Long userId) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Professor"), false);
		classService.promoteToMonitor(classId, userId);
		return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{classId}/demote/{userId}")
    public ResponseEntity<?> demoteToStudent(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId, 
    		@PathVariable Long userId) {
		tokenService.validateTokenAndPermissions(token, List.of("Admin", "Professor"), false);
		classService.demoteToStudent(classId, userId);
		return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{classId}/report")
    public ResponseEntity<ReportDTO> getClassReport(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId) {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Aluno", "Monitor", "Professor"), false);
        ReportDTO report = reportService.generateReport(classId);
        return ResponseEntity.ok(report);
    }
    
    @GetMapping("/{classId}/report/pdf")
    public ResponseEntity<byte[]> generateReportPdf(
    		@RequestHeader("Authorization") String token,
    		@PathVariable Long classId) throws DocumentException, IOException {
    	tokenService.validateTokenAndPermissions(token, List.of("Admin", "Monitor", "Professor"), false);
        byte[] pdfContent = reportService.generateReportPdf(classId);
        // Retornar o PDF como resposta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=report.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}
