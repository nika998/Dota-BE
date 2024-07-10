package com.artigo.dota.service;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.exception.MailNotSentException;
import com.artigo.dota.exception.OrderItemsNonAvailableException;

import java.util.ArrayList;
import java.util.List;

public interface OrderService {

    OrderDTO processOrder(OrderDTO orderDTO) throws OrderItemsNonAvailableException, MailNotSentException;

    ArrayList<OrderItemDTO> checkOrder(List<OrderItemDTO> orderItems);

    OrderDO saveOrder(OrderDTO orderDTO);

    OrderDTO deleteOrder(Long orderId);
}
