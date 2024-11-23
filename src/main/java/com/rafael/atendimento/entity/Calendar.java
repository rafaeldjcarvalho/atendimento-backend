package com.rafael.atendimento.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calendars")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Calendar {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @JsonIgnoreProperties("calendars")
    private Class clazz; // Turma à qual o calendário pertence
	
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"myClasses", "monitorClass", "alunosClasses", "professoresClasses"})
    private User owner; // Usuário responsável (professor ou monitor)
	
	@OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> schedules; // Lista de horários
	
}
