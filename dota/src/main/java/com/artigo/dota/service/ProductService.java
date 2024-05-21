package com.artigo.dota.service;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductDetailsSubmitDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.dto.ProductSubmitDTO;
import com.artigo.dota.entity.ProductDetailsDO;
import com.artigo.dota.exception.ImageProcessingException;
import com.artigo.dota.exception.ProductNotProcessedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    Page<ProductDTO> getProductsByPage(Pageable pageable);

    ProductDTO getProductById(Long id);

    ProductDetailsDO processProductDetails(ProductSubmitDTO product, ProductDetailsSubmitDTO productDetails, List<MultipartFile> files) throws ImageProcessingException, ProductNotProcessedException;

    ProductDTO processProduct(ProductSubmitDTO product, List<MultipartFile> files) throws ImageProcessingException, ProductNotProcessedException;
}
