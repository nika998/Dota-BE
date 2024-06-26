package com.artigo.dota.controller;

import com.artigo.dota.dto.*;
import com.artigo.dota.exception.ImageProcessingException;
import com.artigo.dota.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
            @RequestPart("product")@Valid ProductSubmitDTO product,
            @RequestPart("files") List<MultipartFile> files) {

        try {
            ProductDTO processedProduct = productService.processProduct(product, files);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(processedProduct);

        }catch (ImageProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }

    }
}
