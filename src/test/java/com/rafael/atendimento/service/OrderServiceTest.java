package com.rafael.atendimento.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.rafael.atendimento.dto.OrderPageDTO;
import com.rafael.atendimento.dto.OrderServiceDTO;
import com.rafael.atendimento.dto.mapper.OrderMapper;
import com.rafael.atendimento.entity.OrderService;
import com.rafael.atendimento.repository.OrderServiceRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderServiceRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ClassService classService;

    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceService orderService;

    private OrderService mockOrder;
    private OrderServiceDTO mockOrderDTO;

    @BeforeEach
    void setUp() {
        mockOrder = new OrderService();
        mockOrder.setId(1L);
        
        mockOrderDTO = new OrderServiceDTO(1L, "Pedido Teste", "Descrição", null, null, null, "PENDING", 1L, 1L);
    }

    @Test
    void testFindAllPages_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderService> orderPage = new PageImpl<>(List.of(mockOrder), pageable, 1);

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);
        when(orderMapper.toDTO(any(OrderService.class))).thenReturn(mockOrderDTO);

        OrderPageDTO result = orderService.findAllPages(0, 10);

        assertNotNull(result);
        assertEquals(1, result.totalElementos());
        assertEquals(1, result.totalPages());
        assertEquals(1, result.orders().size());
    }

    @Test
    void testFindAllPages_Empty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderService> orderPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);

        OrderPageDTO result = orderService.findAllPages(0, 10);

        assertNotNull(result);
        assertEquals(0, result.totalElementos());
        assertEquals(0, result.totalPages());
        assertTrue(result.orders().isEmpty());
    }

    @Test
    void testFindById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        when(orderMapper.toDTO(mockOrder)).thenReturn(mockOrderDTO);

        OrderServiceDTO result = orderService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void testFindById_NotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> orderService.findById(999L));
    }

    @Test
    void testCreate_Success() {
        when(orderMapper.toEntity(any(OrderServiceDTO.class))).thenReturn(mockOrder);
        when(orderRepository.save(any(OrderService.class))).thenReturn(mockOrder);
        when(orderMapper.toDTO(any(OrderService.class))).thenReturn(mockOrderDTO);

        OrderServiceDTO result = orderService.create(mockOrderDTO);

        assertNotNull(result);
        assertEquals(mockOrderDTO.id(), result.id());
    }

    @Test
    void testUpdate_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(OrderService.class))).thenReturn(mockOrder);
        when(orderMapper.toDTO(any(OrderService.class))).thenReturn(mockOrderDTO);

        OrderServiceDTO result = orderService.update(1L, mockOrderDTO);

        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void testUpdate_NotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> orderService.update(999L, mockOrderDTO));
    }

    @Test
    void testDelete_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        
        assertDoesNotThrow(() -> orderService.delete(1L));
        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResponseStatusException.class, () -> orderService.delete(999L));
    }

    @Test
    void testGetOrdersByClass_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderService> orderPage = new PageImpl<>(List.of(mockOrder), pageable, 1);

        when(orderRepository.findByClazz_Id(anyLong(), any(Pageable.class))).thenReturn(orderPage);
        when(orderMapper.toDTO(any(OrderService.class))).thenReturn(mockOrderDTO);

        OrderPageDTO result = orderService.getOrdersByClass(1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.totalElementos());
    }

    @Test
    void testGetOrdersByClass_Empty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderService> orderPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(orderRepository.findByClazz_Id(anyLong(), any(Pageable.class))).thenReturn(orderPage);

        OrderPageDTO result = orderService.getOrdersByClass(1L, 0, 10);

        assertNotNull(result);
        assertTrue(result.orders().isEmpty());
    }

    @Test
    void testGetOrdersByUser_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderService> orderPage = new PageImpl<>(List.of(mockOrder), pageable, 1);

        when(orderRepository.findByOwner_Id(anyLong(), any(Pageable.class))).thenReturn(orderPage);
        when(orderMapper.toDTO(any(OrderService.class))).thenReturn(mockOrderDTO);

        OrderPageDTO result = orderService.getOrdersByUser(1L, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.totalElementos());
    }

    @Test
    void testGetOrdersByUser_Empty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderService> orderPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(orderRepository.findByOwner_Id(anyLong(), any(Pageable.class))).thenReturn(orderPage);

        OrderPageDTO result = orderService.getOrdersByUser(1L, 0, 10);

        assertNotNull(result);
        assertTrue(result.orders().isEmpty());
    }
}

