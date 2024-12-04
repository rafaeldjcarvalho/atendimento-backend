package com.rafael.atendimento.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;
import com.rafael.atendimento.enums.converters.TypeAcessConverter;
import com.rafael.atendimento.enums.converters.UserStatusConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	@NotNull
	@Length(min = 3, max = 100)
	@Column(length = 100, nullable = false)
	private String name;
	
	@NotBlank
	@NotNull
	@Length(min = 5, max = 100)
	@Column(length = 100, nullable = false)
	private String email;
	
	@NotBlank
	@NotNull
	@Length(min = 6, max = 100)
	@Column(length = 50, nullable = false)
	private String password;
	
	@NotNull
    @Column(length = 10, nullable = false)
    @Convert(converter = TypeAcessConverter.class)
	private TypeAccess typeAccess;
	
	@NotNull
    @Column(length = 10, nullable = false)
    @Convert(converter = UserStatusConverter.class)
	private UserStatus status = UserStatus.ATIVO;
	
	@OneToMany(mappedBy = "owner")
	@JsonIgnoreProperties("owner")
	private List<Class> myClasses;
	
	@ManyToOne
	@JsonIgnoreProperties("monitores")
	@JoinColumn(name = "class_id")
	private Class monitorClass;  // monitores
	
	@ManyToMany(mappedBy = "alunos")
	@JsonIgnoreProperties("alunos")
	private List<Class> alunosClasses;  // alunos
	
	@ManyToMany(mappedBy = "professores")
	@JsonIgnoreProperties("professores")
	private List<Class> professoresClasses;  // professores
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("owner")
    private List<OrderService> orders;
	
	// Métodos
//	
//	public void addClass(Class turma) {
//	    switch (this.typeAccess) {
//	        case ALUNO -> {
//	            if (!this.alunosClasses.contains(turma)) {
//	                this.alunosClasses.add(turma);
//	                turma.getAlunos().add(this);
//	            }
//	        }
//	        case PROFESSOR -> {
//	            if (!this.professoresClasses.contains(turma)) {
//	                this.professoresClasses.add(turma);
//	                turma.getProfessores().add(this);
//	            }
//	        }
//	        case MONITOR -> {
//	            if (this.monitorClass == null) {
//	                this.monitorClass = turma;
//	                turma.getMonitores().add(this);
//	            } else {
//	                throw new IllegalStateException("O monitor já pertence a uma turma");
//	            }
//	        }
//	        default -> throw new IllegalArgumentException("Tipo de usuário inválido");
//	    }
//	}
//
//	public void removeClass(Class turma) {
//	    switch (this.typeAccess) {
//	        case ALUNO -> {
//	            if (this.alunosClasses.contains(turma)) {
//	                this.alunosClasses.remove(turma);
//	                turma.getAlunos().remove(this);
//	            }
//	        }
//	        case PROFESSOR -> {
//	            if (this.professoresClasses.contains(turma)) {
//	                this.professoresClasses.remove(turma);
//	                turma.getProfessores().remove(this);
//	            }
//	        }
//	        case MONITOR -> {
//	            if (this.monitorClass != null && this.monitorClass.equals(turma)) {
//	                this.monitorClass = null;
//	                turma.getMonitores().remove(this);
//	            }
//	        }
//	        default -> throw new IllegalArgumentException("Tipo de usuário inválido");
//	    }
//	}

}
