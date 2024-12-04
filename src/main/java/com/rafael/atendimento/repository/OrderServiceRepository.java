package com.rafael.atendimento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafael.atendimento.entity.OrderService;

public interface OrderServiceRepository extends JpaRepository<OrderService, Long> {
	List<OrderService> findByClazz_Id(Long classId);
    List<OrderService> findByOwner_Id(Long userId);
}
