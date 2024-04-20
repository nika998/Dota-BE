package com.artigo.dota.service.impl;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.exception.OrderItemsNonAvailableException;
import com.artigo.dota.mapper.OrderItemMapper;
import com.artigo.dota.mapper.OrderMapper;
import com.artigo.dota.repository.OrderRepository;
import com.artigo.dota.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final EmailService emailService;

    private final OrderItemService orderItemService;

    private final ProductDetailsService productDetailsService;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    @Value("${spring.mail.username}")
    private String mailDefaultRecipient;

    public OrderServiceImpl(OrderRepository orderRepository, EmailService emailService, OrderMapper orderMapper, OrderItemMapper orderItemMapper, OrderItemService orderItemService, ProductDetailsService productDetailsService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderItemService = orderItemService;
        this.productDetailsService = productDetailsService;
    }

    @Override
    public OrderDTO checkOrderAndSendMail(OrderDTO orderDTO, List<OrderItemDTO> orderItems) throws OrderItemsNonAvailableException {
        OrderDO orderDO = orderMapper.dtoToEntity(orderDTO);

        var orderItemsNotAvailable = new ArrayList<OrderItemDO>();
        var orderItemsAvailable = new ArrayList<OrderItemDO>();

        OrderDO savedOrder = checkAndSaveOrder(orderItemsAvailable, orderItemsNotAvailable, orderItems, orderDO);

        List<String> recipientsList = new ArrayList<>(Collections.singletonList(mailDefaultRecipient));
        emailService.sendOrderMail(recipientsList, savedOrder);

        if(orderItemsNotAvailable.isEmpty() && savedOrder != null)
            return orderMapper.entityToDto(savedOrder);

        orderDTO.setOrderItems(orderItemsNotAvailable.stream().map(orderItemMapper::entityToDto).toList());
        throw new OrderItemsNonAvailableException("Some of the ordered items are not available", orderDTO);
    }

    @Override
    @Transactional
    public OrderDO checkAndSaveOrder(ArrayList<OrderItemDO> availableItems, ArrayList<OrderItemDO> notAvailableItems, List<OrderItemDTO> orderItems, OrderDO orderDO){
        orderItems.stream()
                .map(orderItemMapper::dtoToEntity)
                .forEach(orderItemDO -> {
                    if (!productDetailsService.reduceProductQuantity(orderItemDO.getProductDetails().getId(), orderItemDO.getQuantity())) {
                        notAvailableItems.add(orderItemDO);
                    } else {
                        availableItems.add(orderItemDO);
                    }

                });

        if (!availableItems.isEmpty()) {

            orderDO.setCreatedAt(LocalDateTime.now());
            OrderDO savedOrder = orderRepository.save(orderDO);

            List<OrderItemDO> orderItemDOs = availableItems.stream()
                    .map(orderItemDO -> {
                        orderItemDO.setOrder(savedOrder.getId());
                        return orderItemDO;
                    })
                    .toList();
            savedOrder.setOrderItems(orderItemService.saveAll(orderItemDOs));

            return savedOrder;
        }

        return null;
    }

}
