package com.artigo.dota.controller;

import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductImageDTO;
import com.artigo.dota.dto.ProductImageUrlDTO;
import com.artigo.dota.dto.ProductSubmitDTO;
import com.artigo.dota.mapper.ProductMapper;
import com.artigo.dota.service.ProductImageService;
import com.artigo.dota.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductImageService productImageService, ProductMapper productMapper) {
        this.productService = productService;
        this.productImageService = productImageService;
        this.productMapper = productMapper;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductDTO>> getProductsByPage(Pageable pageable) {
        return ResponseEntity.ok(productService.getProductsByPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<?> saveProduct(
            @RequestPart("data") ProductSubmitDTO product,
            @RequestPart("files") List<MultipartFile> files) {

        if(files.size() != product.getImages().size()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image files do not match with ProductImageDTOs");
        }

        for (MultipartFile file : files) {
            // Get the productId associated with the file
            if(file.getOriginalFilename() != null && file.getOriginalFilename().contains(".")) {
                String[] parts = file.getOriginalFilename().split("\\.");
                Long productId = Long.parseLong(parts[0]);

                // Find the corresponding ProductImageDTO using the productId
                ProductImageDTO productImageDTO = product.getImages()
                        .stream()
                        .filter(image -> productId.equals(image.getProductImageId()))
                        .findFirst()
                        .orElse(null);

                if (productImageDTO != null) {
                    // Associate the file with the corresponding ProductImageDTO
                    productImageDTO.setFile(file);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image files do not match with ProductImageDTOs");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image files are not named properly");
            }

        }

        List<ProductImageUrlDTO> uploadedImagesDTO =
                productImageService.uploadProductImages(product.getImages(), product.getType(), product.getName());

        if(uploadedImagesDTO.size() < product.getImages().size()) {
            productImageService.deleteUploadedImages(uploadedImagesDTO);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save Product");
        }

        try {
            ProductDTO productDTO = productMapper.submitDtoToDto(product);

            ProductDTO savedProduct = productService.saveProduct(productDTO, uploadedImagesDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedProduct);

        } catch (RuntimeException e) {
            productImageService.deleteUploadedImages(uploadedImagesDTO);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save Product");
        }

    }
}
