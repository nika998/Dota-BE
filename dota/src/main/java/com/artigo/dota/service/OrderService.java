package com.artigo.dota.service;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;

import java.util.List;

public interface OrderService {


    OrderDTO saveOrder(OrderDTO orderDTO, List<OrderItemDTO> orderItems);
}
