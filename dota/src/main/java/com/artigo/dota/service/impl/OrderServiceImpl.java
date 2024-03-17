package com.artigo.dota.service.impl;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.mapper.OrderItemMapper;
import com.artigo.dota.mapper.OrderMapper;
import com.artigo.dota.repository.OrderRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.OrderItemService;
import com.artigo.dota.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final EmailService emailService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemService orderItemService;
    @Value("${spring.mail.username}")
    private String mailDefaultRecipient;

    public OrderServiceImpl(OrderRepository orderRepository, EmailService emailService, OrderMapper orderMapper, OrderItemMapper orderItemMapper, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderItemService = orderItemService;
    }

    @Override
    @Transactional
    public OrderDTO saveOrder(OrderDTO orderDTO, List<OrderItemDTO> orderItems) {
        OrderDO orderDO = orderMapper.dtoToEntity(orderDTO);
        orderDO.setCreatedAt(LocalDateTime.now());
        OrderDO savedOrder = orderRepository.save(orderDO);

        List<OrderItemDO> orderItemDOs = orderItems.stream()
                .map(orderItemDTO -> {
                    OrderItemDO orderItemDO = orderItemMapper.dtoToEntity(orderItemDTO);
                    orderItemDO.setOrder(savedOrder.getId());
                    return orderItemDO;
                })
                .toList();

        savedOrder.setOrderItems(orderItemService.saveAll(orderItemDOs));

        List<String> recipientsList = new ArrayList<>(Arrays.asList(mailDefaultRecipient));
        emailService.sendOrderMail(recipientsList, savedOrder);

        return orderMapper.entityToDto(savedOrder);
    }

}
