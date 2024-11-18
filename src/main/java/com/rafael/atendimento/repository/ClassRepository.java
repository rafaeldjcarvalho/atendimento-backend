package com.rafael.atendimento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafael.atendimento.entity.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {
	Optional<Class> findByName(String name);
}
