package com.vinicius.ecommerce.services;

import com.vinicius.ecommerce.model.Product;
import com.vinicius.ecommerce.records.data.ProductDetailsDTO;
import com.vinicius.ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductRepository repository;

    public ProductDetailsDTO createProduct(Product product, MultipartFile image) {
        String generatedImage = null;

        if(image.isEmpty()) {
            throw new RuntimeException("Image not provided!");
        }

        generatedImage = s3Service.uploadImage(image);

        if(generatedImage == null) {
            throw new RuntimeException("Error uploading image to s3 bucket!");
        }

        product.setImageUrl(generatedImage);
        var productSave = repository.save(product);
        return new ProductDetailsDTO(productSave);
    }

    public ProductDetailsDTO findById(Long id) {
        var product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        return new ProductDetailsDTO(product);
    }
}
