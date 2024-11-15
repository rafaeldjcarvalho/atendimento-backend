package com.rafael.atendimento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafael.atendimento.entity.User;

public interface UserRepository extends JpaRepository<User, String>  {
	Optional<User> findByEmail(String email);
}
