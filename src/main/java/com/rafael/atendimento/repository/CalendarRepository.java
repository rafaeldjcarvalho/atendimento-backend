package com.rafael.atendimento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.atendimento.entity.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findByClazzId(Long classId);

    Optional<Calendar> findByIdAndClazzId(Long calendarId, Long classId);
}