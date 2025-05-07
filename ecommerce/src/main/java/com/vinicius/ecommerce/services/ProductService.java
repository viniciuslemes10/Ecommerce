package com.vinicius.ecommerce.services;

import com.vinicius.ecommerce.model.Product;
import com.vinicius.ecommerce.records.data.ProductDTO;
import com.vinicius.ecommerce.records.data.ProductDetailsDTO;
import com.vinicius.ecommerce.repositories.ProductRepository;
import com.vinicius.ecommerce.utils.UpdateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private UpdateValues updateValues;

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

    public Page<ProductDetailsDTO> getAllProducts(Pageable pageable) {
        var pageProduct = repository.findAll(pageable);
        return pageProduct.map(ProductDetailsDTO::new);
    }

    public ProductDetailsDTO update(ProductDTO data, Long id) {
        var product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        updateValues.updateIfNotNullOrEmpty(data.name(), product::setName);
        updateValues.updateIfNotNullOrEmpty(data.description(), product::setDescription);
        updateValues.updateIfNotNullOrEmpty(data.stock(), product::setStock);
        updateValues.updateIfNotNullOrEmpty(data.price(), product::setPrice);

        if(data.image() != null && !data.image().isEmpty()) {
            String imageUpdate = s3Service.updateImage(product, data.image());
            product.setImageUrl(imageUpdate);
        }

        var productSave = repository.save(product);
        return new ProductDetailsDTO(productSave);
    }

    public void delete(Long id) {
        var product = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found!"));
        String filename = s3Service.formatImageName(product.getImageUrl());
        s3Service.deleteImage(filename);
        repository.delete(product);
    }
}
