package com.artigo.dota.service.impl;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.exception.MailNotSentException;
import com.artigo.dota.exception.OrderItemsNonAvailableException;
import com.artigo.dota.mapper.OrderItemMapper;
import com.artigo.dota.mapper.OrderMapper;
import com.artigo.dota.repository.OrderRepository;
import com.artigo.dota.service.*;
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

    public OrderServiceImpl(OrderRepository orderRepository, EmailService emailService, OrderMapper orderMapper, OrderItemMapper orderItemMapper, OrderItemService orderItemService, ProductDetailsService productDetailsService) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderItemService = orderItemService;
        this.productDetailsService = productDetailsService;
    }

    @Override
    @Transactional
    public OrderDTO processOrder(OrderDTO orderDTO) throws OrderItemsNonAvailableException, MailNotSentException {

        var itemsRecentlyNotAvailable  = checkOrder(orderDTO.getOrderItems());
        if(!itemsRecentlyNotAvailable.isEmpty()) {
            orderDTO.setOrderItems(itemsRecentlyNotAvailable);
            throw new OrderItemsNonAvailableException("Some of the ordered items are not available", orderDTO);
        }

        OrderDO savedOrder = saveOrder(orderDTO);

        emailService.sendOrderMails(savedOrder);

        return orderMapper.entityToDto(savedOrder);
    }

    @Override
    public ArrayList<OrderItemDTO> checkOrder(List<OrderItemDTO> orderItems) {
        var itemsRecentlyNotAvailable = new ArrayList<OrderItemDTO>();
        orderItems.stream()
                .forEach(orderItemDTO -> {
                    if(Boolean.TRUE.equals(orderItemDTO.getIsAvailable()) && !productDetailsService.checkProductAvailability(orderItemDTO.getProductDetailsId(), orderItemDTO.getQuantity())) {
                        orderItemDTO.setIsAvailable(false);
                        itemsRecentlyNotAvailable.add(orderItemDTO);
                    }
                });
        return itemsRecentlyNotAvailable;
    }

    @Override
    @Transactional
    public OrderDO saveOrder(OrderDTO orderDTO){

        List<OrderItemDO> orderItemDOs = orderDTO.getOrderItems().stream()
                        .map(orderItemMapper::dtoToEntity).toList();

        orderDTO.setOrderItems(null);
        OrderDO orderDO = orderMapper.dtoToEntity(orderDTO);
        orderDO.setCreatedAt(LocalDateTime.now());

        OrderDO savedOrder = orderRepository.save(orderDO);

        savedOrder.setOrderItems(orderItemService.saveAll(orderItemDOs.stream().peek(orderItemDO -> orderItemDO.setOrderId(savedOrder.getId())).toList()));

        return savedOrder;
    }

}
