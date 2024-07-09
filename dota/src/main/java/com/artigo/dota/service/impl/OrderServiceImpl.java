package com.artigo.dota.service.impl;

import com.artigo.dota.dto.OrderDTO;
import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderDO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.exception.EntityNotFoundException;
import com.artigo.dota.exception.MailNotSentException;
import com.artigo.dota.exception.OrderItemsNonAvailableException;
import com.artigo.dota.mapper.OrderItemMapper;
import com.artigo.dota.mapper.OrderMapper;
import com.artigo.dota.repository.OrderRepository;
import com.artigo.dota.service.*;
import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static final String PRODUCT_DETAIL_FILTER = "deletedProductFilter";

    private static final String PRODUCT_IMAGE_FILTER = "deletedImageFilter";

    private static final String DELETED_PARAM = "isDeleted";

    private final EntityManager entityManager;

    private final OrderRepository orderRepository;

    private final EmailService emailService;

    private final OrderItemService orderItemService;

    private final ProductDetailsService productDetailsService;

    private final OrderMapper orderMapper;

    private final OrderItemMapper orderItemMapper;

    public OrderServiceImpl(EntityManager entityManager, OrderRepository orderRepository, EmailService emailService, OrderMapper orderMapper, OrderItemMapper orderItemMapper, OrderItemService orderItemService, ProductDetailsService productDetailsService) {
        this.entityManager = entityManager;
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderItemService = orderItemService;
        this.productDetailsService = productDetailsService;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"products", "product"}, allEntries = true)
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

        orderItemDOs.forEach(orderItemDO -> orderItemDO.setOrderId(savedOrder.getId()));
        savedOrder.setOrderItems(orderItemService.saveAll(orderItemDOs));

        savedOrder.getOrderItems().forEach(orderItemDO ->
            productDetailsService.reduceProductQuantity(orderItemDO.getProductDetails().getId(), orderItemDO.getQuantity())
        );

        return savedOrder;
    }

    @Override
    public List<OrderDO> findOrdersByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, boolean includeDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter productFilter  = session.enableFilter(PRODUCT_DETAIL_FILTER);
        Filter imageFilter = session.enableFilter(PRODUCT_IMAGE_FILTER);

        if (includeDeleted) {
            productFilter .setParameter(DELETED_PARAM, true);
            imageFilter.setParameter(DELETED_PARAM, true);
        } else {
            productFilter.setParameter(DELETED_PARAM, false);
            imageFilter.setParameter(DELETED_PARAM, false);
        }

        List<OrderDO> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

        if (includeDeleted) {
            session.disableFilter(PRODUCT_DETAIL_FILTER);
            session.disableFilter(PRODUCT_IMAGE_FILTER);
        }
        return orders;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public OrderDTO deleteOrder(Long orderId) {
        var orderToDelete = findOrderToDeleteById(orderId, true).orElseThrow(() -> new EntityNotFoundException("Order with provided id not found"));
        var orderItemsToDelete = orderItemService.deleteOrderItemsList(orderToDelete.getOrderItems());
        orderToDelete.setIsDeleted(Boolean.TRUE);
        orderItemsToDelete.forEach(orderItemDO ->
            orderItemDO.getProductDetails().setQuantity(orderItemDO.getProductDetails().getQuantity() + orderItemDO.getQuantity())
        );


        var deletedOrder = orderRepository.save(orderToDelete);
        return orderMapper.entityToDto(deletedOrder);
    }

    public Optional<OrderDO> findOrderToDeleteById(Long orderId, boolean includeDeleted) {
        Session session = entityManager.unwrap(Session.class);
        Filter productFilter  = session.enableFilter(PRODUCT_DETAIL_FILTER);
        Filter imageFilter = session.enableFilter(PRODUCT_IMAGE_FILTER);

        if (includeDeleted) {
            productFilter .setParameter(DELETED_PARAM, true);
            imageFilter.setParameter(DELETED_PARAM, true);
        } else {
            productFilter.setParameter(DELETED_PARAM, false);
            imageFilter.setParameter(DELETED_PARAM, false);
        }

        Optional<OrderDO> orderToDelete = orderRepository.findById(orderId);

        if (includeDeleted) {
            session.disableFilter(PRODUCT_DETAIL_FILTER);
            session.disableFilter(PRODUCT_IMAGE_FILTER);
        }
        return orderToDelete;
    }

}
