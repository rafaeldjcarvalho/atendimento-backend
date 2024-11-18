package com.rafael.atendimento.entity;

import org.hibernate.validator.constraints.Length;

import com.rafael.atendimento.enums.TypeAccess;
import com.rafael.atendimento.enums.UserStatus;
import com.rafael.atendimento.enums.converters.TypeAcessConverter;
import com.rafael.atendimento.enums.converters.UserStatusConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

}
