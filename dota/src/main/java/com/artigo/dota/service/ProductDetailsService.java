package com.artigo.dota.service;

import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.entity.ProductDetailsDO;

public interface ProductDetailsService {

    boolean reduceProductQuantity(long productDetailsId, int orderedQuantity);

    boolean checkProductAvailability(long productDetailsId, int orderedQuantity);

    ProductDO getRelatedProduct(ProductDetailsDO productDetailsDO);
}
