package com.artigo.dota.controller;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.exception.OrderItemsNonAvailableException;
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
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.checkOrderAndSendMail(orderDTO, orderItemDTOList));
        } catch (OrderItemsNonAvailableException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getOrderDTO());
        }
    }
}
