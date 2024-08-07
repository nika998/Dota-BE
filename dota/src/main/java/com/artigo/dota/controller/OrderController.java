package com.artigo.dota.controller;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.exception.MailNotSentException;
import com.artigo.dota.exception.OrderItemsNonAvailableException;
import com.artigo.dota.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody@Valid OrderDTO orderDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.processOrder(orderDTO));
        } catch (OrderItemsNonAvailableException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getOrderDTO());
        } catch (MailNotSentException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOrder(@RequestParam Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderId));
    }
}
