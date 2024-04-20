package com.artigo.dota.exception;

import com.artigo.dota.dto.OrderDTO;

public class OrderItemsNonAvailableException extends RuntimeException{

    private OrderDTO orderDTO;

    public OrderItemsNonAvailableException(String message, OrderDTO orderDTO) {
        super(message);
        this.orderDTO = orderDTO;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }
}
