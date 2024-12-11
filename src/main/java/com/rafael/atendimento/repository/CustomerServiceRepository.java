package com.rafael.atendimento.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rafael.atendimento.entity.CustomerService;

@Repository
public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {
	
    Page<CustomerService> findByClazz_Id(Long classId, Pageable pageable);
    Page<CustomerService> findByOwner_Id(Long ownerId, Pageable pageable);
    Page<CustomerService> findByStudent_Id(Long studentId, Pageable pageable);
}
