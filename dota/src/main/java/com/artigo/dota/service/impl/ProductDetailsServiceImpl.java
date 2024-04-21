package com.artigo.dota.service.impl;

import com.artigo.dota.entity.ProductDO;
import com.artigo.dota.entity.ProductDetailsDO;
import com.artigo.dota.repository.ProductDetailsRepository;
import com.artigo.dota.repository.ProductRepository;
import com.artigo.dota.service.ProductDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;

    private final ProductRepository productRepository;

    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepository, ProductRepository productRepository) {
        this.productDetailsRepository = productDetailsRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public boolean reduceProductQuantity(long productDetailsId, int orderedQuantity) {
        var productDetailsDO = productDetailsRepository.findById(productDetailsId);
        if(productDetailsDO.isPresent() && productDetailsDO.get().getQuantity() >= orderedQuantity) {
            productDetailsDO.get().setQuantity(productDetailsDO.get().getQuantity() - orderedQuantity);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkProductAvailability(long productDetailsId, int orderedQuantity) {
        var productDetailsDO = productDetailsRepository.findById(productDetailsId);
        if(productDetailsDO.isPresent() && productDetailsDO.get().getQuantity() >= orderedQuantity) {
            return true;
        }
        return false;
    }

    @Override
    public ProductDO getRelatedProduct(ProductDetailsDO productDetailsDO) {
        return productRepository.findById(productDetailsDO.getProductId()).orElse(null);
    }
}
