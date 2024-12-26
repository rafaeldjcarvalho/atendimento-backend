package com.rafael.atendimento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rafael.atendimento.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
	Optional<User> findByEmail(String email);
	
	@Query("SELECT u FROM User u JOIN u.alunosClasses a WHERE a.id = :classId")
    List<User> findStudentsByClassId(@Param("classId") Long classId);
	
	@Query("SELECT u FROM User u JOIN u.professoresClasses a WHERE a.id = :classId")
    List<User> findTeachersByClassId(@Param("classId") Long classId);
	
	@Query("SELECT u FROM User u JOIN u.monitorClass a WHERE a.id = :classId")
    List<User> findMonitorByClassId(@Param("classId") Long classId);
}
