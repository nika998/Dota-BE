package com.artigo.dota.service.impl;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.mapper.OrderMapper;
import com.artigo.dota.repository.OrderRepository;
import com.artigo.dota.service.EmailService;
import com.artigo.dota.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderMapper orderMapper;

    @Value("${spring.mail.username}")
    private String mailDefaultRecipient;

    @Override
    @Transactional
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        OrderDO savedOrder = orderRepository.save(orderMapper.dtoToEntity(orderDTO));
        if(savedOrder != null) {
            List<String> recipientsList = new ArrayList<>(Arrays.asList(mailDefaultRecipient));
            emailService.sendOrderMail(recipientsList, savedOrder);
        }
        return orderMapper.entityToDto(savedOrder);
    }

}
