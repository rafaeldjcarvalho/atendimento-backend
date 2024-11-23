package com.rafael.atendimento.entity;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
    private String dayOfWeek; // Exemplo: "MONDAY", "TUESDAY"
	
	@NotNull
    private LocalTime startTime;
	
	@NotNull
    private LocalTime endTime;
	
	@ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    @JsonIgnoreProperties("schedules")
    private Calendar calendar;
}
