package com.vinicius.ecommerce.services;

import com.vinicius.ecommerce.exceptions.CategoryNotFoudException;
import com.vinicius.ecommerce.exceptions.ImageNotProvidedException;
import com.vinicius.ecommerce.exceptions.ImageUploadException;
import com.vinicius.ecommerce.exceptions.ProductNotFoundException;
import com.vinicius.ecommerce.model.Product;
import com.vinicius.ecommerce.records.data.ProductDTO;
import com.vinicius.ecommerce.records.data.ProductDetailsDTO;
import com.vinicius.ecommerce.repositories.CategoryRepository;
import com.vinicius.ecommerce.repositories.ProductRepository;
import com.vinicius.ecommerce.utils.ProductSpecification;
import com.vinicius.ecommerce.utils.ProductValidator;
import com.vinicius.ecommerce.utils.UpdateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
public class ProductService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UpdateValues updateValues;

    public ProductDetailsDTO createProduct(Product product, MultipartFile image, Long categoryId) {
        ProductValidator.validateProduct(product);
        String generatedImage = null;

        if(image.isEmpty()) {
            throw new ImageNotProvidedException();
        }

        generatedImage = s3Service.uploadImage(image);

        if(generatedImage == null) {
            throw new ImageUploadException();
        }

        product.setImageUrl(generatedImage);

        var category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoudException::new);

        product.setCategory(category);

        var productSave = repository.save(product);
        return new ProductDetailsDTO(productSave);
    }

    public ProductDetailsDTO findById(Long id) {
        var product = repository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        return new ProductDetailsDTO(product);
    }

    public Page<ProductDetailsDTO> getAllProducts(
            String name,
            BigDecimal priceMin,
            BigDecimal priceMax,
            Pageable pageable) {
        Specification<Product> specification = ProductSpecification.withFilters(name, priceMin, priceMax);
        return repository.findAll(specification, pageable).map(ProductDetailsDTO::new);
    }

    public ProductDetailsDTO update(ProductDTO data, Long id) {
        var product = repository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

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
                .orElseThrow(ProductNotFoundException::new);
        String filename = s3Service.formatImageName(product.getImageUrl());
        s3Service.deleteImage(filename);
        repository.delete(product);
    }
}
