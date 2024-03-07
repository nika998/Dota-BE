package com.artigo.dota.controller;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO savedOrder = this.orderService.saveOrder(orderDTO);
        return savedOrder != null ? ResponseEntity.ok(savedOrder) : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body((OrderDTO) null);
    }
}
