package com.artigo.dota.service;

import com.artigo.dota.dto.ProductDetailQuantityDTO;
import com.artigo.dota.dto.ProductDetailsDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.entity.ProductDetailsDO;

import java.util.List;
import java.util.UUID;

public interface ProductDetailsService {

    boolean reduceProductQuantity(UUID productDetailsId, int orderedQuantity);

    boolean checkProductAvailability(UUID productDetailsId, int orderedQuantity);

    ProductDO getRelatedProduct(ProductDetailsDO productDetailsDO);

    List<ProductDetailsDO> saveAll(List<ProductDetailsDO> productDetailsDOList);

    ProductDetailsDO saveProductDetails(ProductDetailsDTO productDetailsDTO, List<ProductImageUrlDTO> uploadedImagesDTO);

    ProductDetailsDTO getProductById(UUID id);

    List<ProductDetailQuantityDTO> getProductDetailQuantities(List<UUID> idList);

    List<ProductDetailsDO> deleteProductDetailsList(List<ProductDetailsDO> productDetails);

    ProductDetailsDTO deleteProductDetail(UUID id);
}
