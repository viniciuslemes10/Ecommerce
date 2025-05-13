package com.vinicius.ecommerce.controllers;

import com.vinicius.ecommerce.exceptions.InvalidCategoryException;
import com.vinicius.ecommerce.records.data.CategoryDTO;
import com.vinicius.ecommerce.records.data.CategoryDetailsDTO;
import com.vinicius.ecommerce.records.data.ProductsFromCategoryDTO;
import com.vinicius.ecommerce.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryDetailsDTO> create(@RequestBody CategoryDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(data));
    }

    @GetMapping
    public ResponseEntity<List<CategoryDetailsDTO>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDetailsDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryDetailsDTO> update(@PathVariable("id") Long id,
                                                     @RequestBody CategoryDTO data) {
        return ResponseEntity.ok(service.update(id, data));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryDetailsDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductsFromCategoryDTO>> getProductsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProductsByCategory(id));
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<CategoryDetailsDTO>> getSortedCategories(
            @RequestParam(defaultValue = "name") String by,
            @RequestParam(defaultValue = "asc") String order) {

        List<String> allowedFields = List.of("id", "name", "description");
        if (!allowedFields.contains(by)) {
            throw new InvalidCategoryException();
        }
        return ResponseEntity.ok(service.getSortedCategories(by, order));
    }
}
