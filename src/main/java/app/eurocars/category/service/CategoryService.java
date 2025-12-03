package app.eurocars.category.service;

import app.eurocars.category.model.Category;
import app.eurocars.category.repository.CategoryRepository;
import app.eurocars.exception.CategoryNotFound;
import app.eurocars.exception.DomainException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllMainCategories() {
        return categoryRepository.getCategoriesByImageUrlNotNull();
    }

    public List<Category> getAllSubCategories() {
        return categoryRepository.getCategoriesByImageUrlIsNull();
    }

    @Cacheable("categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFound("Category with id " + id + " does not exist"));
    }
}
