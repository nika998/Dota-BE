package com.artigo.dota.service.impl;

import com.artigo.dota.repository.ProductDetailsRepository;
import com.artigo.dota.service.ProductDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;

    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepository) {
        this.productDetailsRepository = productDetailsRepository;
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
}
