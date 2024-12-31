package com.rafael.atendimento.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.atendimento.entity.Attendance;
import com.rafael.atendimento.enums.AttendanceStatus;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	
	long countByUserIdAndStatus(Long userId, AttendanceStatus status);
	long countByUserIdAndStatusAndDateAfter(Long userId, AttendanceStatus status, LocalDate date);
	Optional<Attendance> findByCustomerServiceIdAndUserId(Long customerServiceId, Long userId);
	List<Attendance> findByCustomerServiceId(Long customerServiceId);
}
