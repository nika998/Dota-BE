package com.artigo.dota.controller;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) {
        List<OrderItemDTO> orderItemDTOList = orderDTO.getOrderItems();
        orderDTO.setOrderItems(null);
        OrderDTO savedOrder = this.orderService.saveOrder(orderDTO, orderItemDTOList);
        return savedOrder != null ? ResponseEntity.status(HttpStatus.CREATED).body(savedOrder) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((OrderDTO) null);
    }
}
