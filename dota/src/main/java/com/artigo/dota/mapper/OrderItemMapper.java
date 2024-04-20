package com.artigo.dota.mapper;

import com.artigo.dota.dto.OrderItemDTO;
import com.artigo.dota.entity.OrderItemDO;
import com.artigo.dota.entity.ProductDetailsDO;
import com.artigo.dota.repository.ProductDetailsRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class OrderItemMapper {

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    @Mapping(target = "productDetails", expression = "java(getProductDetails(orderItemDTO.getProductDetailsId()))")
    public abstract OrderItemDO dtoToEntity(OrderItemDTO orderItemDTO);

    @Mapping(target = "productDetailsId", source = "orderItemDO.productDetails.id")
    public abstract OrderItemDTO entityToDto(OrderItemDO orderItemDO);

    protected Boolean mapIsDeleted() {
        return false;
    }

    protected ProductDetailsDO getProductDetails(Long productDetailsId) {
        return productDetailsRepository.findById(productDetailsId)
                .orElseThrow(() -> new RuntimeException("ProductDetailsDO not found for id: " + productDetailsId));
    }
}
