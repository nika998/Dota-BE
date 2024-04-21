package com.artigo.dota.service.impl;

import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.repository.OrderItemRepository;
import com.artigo.dota.repository.OrderRepository;
import com.artigo.dota.service.OrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public List<OrderItemDO> saveAll(List<OrderItemDO> orderItemDOs) {
        return orderItemRepository.saveAll(orderItemDOs);
    }

    @Override
    public OrderDO getRelatedOrder(OrderItemDO orderItemDO) {
        return orderRepository.findById(orderItemDO.getOrderId()).orElse(null);
    }
}
