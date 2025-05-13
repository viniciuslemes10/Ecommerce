package com.vinicius.ecommerce.services;

import com.vinicius.ecommerce.exceptions.CategoryNotFoudException;
import com.vinicius.ecommerce.model.Category;
import com.vinicius.ecommerce.records.data.CategoryDTO;
import com.vinicius.ecommerce.records.data.CategoryDetailsDTO;
import com.vinicius.ecommerce.records.data.ProductsFromCategoryDTO;
import com.vinicius.ecommerce.repositories.CategoryRepository;
import com.vinicius.ecommerce.utils.UpdateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryDetailsDTO create(CategoryDTO data) {
        var category = new Category(data);
        var categorySave = repository.save(category);
        return new CategoryDetailsDTO(categorySave);
    }

    public List<CategoryDetailsDTO> listAll() {
        var categories = repository.findAll();
        return categories.stream()
                .map(CategoryDetailsDTO::new)
                .collect(Collectors.toList());
    }

    public CategoryDetailsDTO findById(Long id) {
        var category = repository.findById(id)
                .orElseThrow(CategoryNotFoudException::new);

        return new CategoryDetailsDTO(category);
    }

    public void delete(Long id) {
        var category = repository.findById(id)
                .orElseThrow(CategoryNotFoudException::new);
        repository.delete(category);
    }

    public CategoryDetailsDTO update(Long id, CategoryDTO data) {
        var category = repository.findById(id)
                .orElseThrow(CategoryNotFoudException::new);

        UpdateValues.updateIfNotNullOrEmpty(data.name(), category::setName);
        UpdateValues.updateIfNotNullOrEmpty(data.description(), category::setDescription);

        var categorySave = repository.save(category);

        return new CategoryDetailsDTO(categorySave);
    }

    public List<CategoryDetailsDTO> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(CategoryDetailsDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProductsFromCategoryDTO> getProductsByCategory(Long id) {
        var category = repository.findById(id)
                .orElseThrow(CategoryNotFoudException::new);

        return category.getProducts()
                .stream()
                .map(ProductsFromCategoryDTO::new)
                .collect(Collectors.toList());
    }

    public List<CategoryDetailsDTO> getSortedCategories(String by, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, by);

        return repository.findAll(sort)
                .stream()
                .map(CategoryDetailsDTO::new)
                .collect(Collectors.toList());
    }
}
