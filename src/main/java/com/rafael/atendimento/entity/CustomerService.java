package com.rafael.atendimento.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.enums.ServiceStatus;
import com.rafael.atendimento.enums.converters.ServiceStatusConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customerServices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerService {
	
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
	
	// Relacionamento com turma
	@ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    @JsonIgnoreProperties("customerServices")
    private Class clazz;
	
	// Relacionamento com professor/monitor
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("customerServices")
    private User owner;
	
	// Relacionamento com aluno
	@ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties("studentCustomerServices")
    private User student;
	
	// Relacionamento com presença
	@OneToMany(mappedBy = "customerService", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("customerService")
	private List<Attendance> attendances;
	
	public void atualizarStatus() {
        if (attendances == null || attendances.isEmpty()) {
            this.status = ServiceStatus.PENDENTE;
            return;
        }

        long presentes = attendances.stream().filter(p -> p.getStatus() == AttendanceStatus.PRESENTE).count();
        long ausentes = attendances.stream().filter(p -> p.getStatus() == AttendanceStatus.AUSENTE).count();

        if (presentes == 2 || (presentes == 1 && ausentes == 1)) {
            this.status = ServiceStatus.CONCLUIDO;
        } else if (ausentes == 2) {
            this.status = ServiceStatus.CANCELADO;
        } else {
            this.status = ServiceStatus.PENDENTE;
        }
    }
}
