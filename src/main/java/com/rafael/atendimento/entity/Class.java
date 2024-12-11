package com.rafael.atendimento.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "classes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Class {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@NotNull
	@Length(min = 3, max = 100)
	@Column(length = 100, nullable = false)
	private String name;
	
	@NotNull
	private LocalDate date;
	
	@ManyToOne
	@JsonIgnoreProperties("myClasses")
	@JoinColumn(name = "owner_id")
	private User owner;
	
	@OneToMany(mappedBy = "monitorClass")
	@JsonIgnoreProperties("monitorClass")
	private List<User> monitores;
	
	@ManyToMany
	@JsonIgnoreProperties("alunosClasses")
	@JoinTable(name = "class_alunos")
	private List<User> alunos;
	
	@ManyToMany
	@JsonIgnoreProperties("professoresClasses")
	@JoinTable(name = "class_professores")
	private List<User> professores;
	
	@OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("clazz")
	private List<Calendar> calendars;
	
	 @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
	 @JsonIgnoreProperties("clazz")
	 private List<OrderService> orders;
	 
	 @OneToMany(mappedBy = "clazz", cascade = CascadeType.ALL, orphanRemoval = true)
	 @JsonIgnoreProperties("clazz")
	 private List<CustomerService> customerServices;
	
	// Métodos
	
	public void addUser(User user) {
	    switch (user.getTypeAccess()) {
	        case ALUNO -> {
	            if (!this.alunos.contains(user)) {
	                this.alunos.add(user);
	                user.getAlunosClasses().add(this);
	            }
	        }
	        case PROFESSOR -> {
	        	if(this.professores == null) {
	        		this.professores = new ArrayList<>();
	        		this.professores.add(user);
	                user.getProfessoresClasses().add(this);
	        	} else if(!this.professores.contains(user)) {
	                this.professores.add(user);
	                user.getProfessoresClasses().add(this);
	        	}
	            
	        }
	        case MONITOR -> {
	            if (!this.monitores.contains(user)) {
	                this.monitores.add(user);
	                user.setMonitorClass(this); // Monitores geralmente pertencem a apenas uma turma
	            }
	        }
	        default -> throw new IllegalArgumentException("Tipo de usuário inválido");
	    }
	}
	
	public void removeUser(User user) {
	    switch (user.getTypeAccess()) {
	        case ALUNO -> {
	            if (this.alunos.contains(user)) {
	                this.alunos.remove(user);
	                user.getAlunosClasses().remove(this);
	            }
	        }
	        case PROFESSOR -> {
	            if (this.professores.contains(user)) {
	                this.professores.remove(user);
	                user.getProfessoresClasses().remove(this);
	            }
	        }
	        case MONITOR -> {
	            if (this.monitores.contains(user)) {
	                this.monitores.remove(user);
	                user.setMonitorClass(null); // Remove a referência da turma no monitor
	            }
	        }
	        default -> throw new IllegalArgumentException("Tipo de usuário inválido");
	    }
	}
	
}
