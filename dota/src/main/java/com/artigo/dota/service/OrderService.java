package com.artigo.dota.service;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.exception.OrderItemsNonAvailableException;

import java.util.ArrayList;
import java.util.List;

public interface OrderService {

    OrderDTO checkOrderAndSendMail(OrderDTO orderDTO, List<OrderItemDTO> orderItems) throws OrderItemsNonAvailableException;

    OrderDO checkAndSaveOrder(ArrayList<OrderItemDO> availableItems, ArrayList<OrderItemDO> notAvailableItems, List<OrderItemDTO> orderItems, OrderDO orderDO);
}
