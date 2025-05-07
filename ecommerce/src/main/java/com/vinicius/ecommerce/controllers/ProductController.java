package com.vinicius.ecommerce.controllers;

import com.vinicius.ecommerce.model.Product;
import com.vinicius.ecommerce.records.data.ProductDetailsDTO;
import com.vinicius.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    ProductService service;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductDetailsDTO> create(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "price") BigDecimal price,
            @RequestParam(value = "image") MultipartFile image,
            @RequestParam(value = "stock") Integer stock
    ) {
        var product = new Product(name, description, price, stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(product, image));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDetailsDTO> findById(@PathVariable("id") Long id) {
        var product = service.findById(id);
        return ResponseEntity.ok(product);
    }
}
