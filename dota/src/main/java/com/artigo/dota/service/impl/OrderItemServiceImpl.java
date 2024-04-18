package com.artigo.dota.service.impl;

import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.repository.OrderItemRepository;
import com.artigo.dota.service.OrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public List<OrderItemDO> saveAll(List<OrderItemDO> orderItemDOs) {
        return orderItemRepository.saveAll(orderItemDOs);
    }
}
