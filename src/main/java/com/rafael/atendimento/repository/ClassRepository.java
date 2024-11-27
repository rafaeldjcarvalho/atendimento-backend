package com.rafael.atendimento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rafael.atendimento.entity.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {
	Optional<Class> findByName(String name);
	
	@Query("SELECT c FROM Class c JOIN c.alunos a WHERE a.id = :userId")
    List<Class> findClassesByAlunoId(@Param("userId") Long userId);
	
	@Query("SELECT c FROM Class c JOIN c.professores p WHERE p.id = :userId")
    List<Class> findClassesByProfessorId(@Param("userId") Long userId);
	
	@Query("SELECT c FROM Class c JOIN c.monitores m WHERE m.id = :userId")
    List<Class> findClassesByMonitorId(@Param("userId") Long userId);
}
