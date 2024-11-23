package com.rafael.atendimento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.atendimento.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByCalendarId(Long calendarId);

    Optional<Schedule> findByIdAndCalendarId(Long scheduleId, Long calendarId);
}