package com.rafael.atendimento.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rafael.atendimento.enums.ServiceStatus;
import com.rafael.atendimento.enums.converters.ServiceStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orderServices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderService {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@NotNull
	@Length(min = 3, max = 100)
	@Column(length = 100, nullable = false)
	private String title;
	
	@NotBlank
	@NotNull
	@Length(min = 3, max = 100)
	@Column(length = 100, nullable = false)
	private String description;
	
	@NotNull
	private LocalDate date;
	
	@NotNull
	private LocalTime time_start;
	
	@NotNull
	private LocalTime time_end;
	
	@NotNull
    @Column(length = 15, nullable = false)
    @Convert(converter = ServiceStatusConverter.class)
	private ServiceStatus status;
	
	// Relacionamento com Turma
    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @JsonIgnoreProperties("orders")
    private Class clazz;

    // Relacionamento com Usuário
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("orders")
    private User owner;

}
