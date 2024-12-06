package com.rafael.atendimento.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rafael.atendimento.entity.OrderService;

public interface OrderServiceRepository extends JpaRepository<OrderService, Long> {
	Page<OrderService> findByClazz_Id(Long classId, Pageable pageable);
    Page<OrderService> findByOwner_Id(Long userId, Pageable pageable);
}
