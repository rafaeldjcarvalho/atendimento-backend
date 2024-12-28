package com.rafael.atendimento.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rafael.atendimento.enums.AttendanceStatus;
import com.rafael.atendimento.enums.converters.AttendanceStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "attendances")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_service_id", nullable = false)
    @JsonIgnoreProperties("attendances")
    private CustomerService customerService;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("attendances")
    private User user;

    @NotNull
    @Column(nullable = false)
    @Convert(converter = AttendanceStatusConverter.class)
    private AttendanceStatus status;

    @NotNull
    private LocalDate date;
    
    @NotNull
    private LocalTime time;

}
