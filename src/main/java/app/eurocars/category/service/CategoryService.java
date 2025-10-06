package app.eurocars.category.service;

import app.eurocars.category.model.Category;
import app.eurocars.category.repository.CategoryRepository;
import app.eurocars.exception.DomainException;
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

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new DomainException("Category with id " + id + " does not exist"));
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new DomainException("Category with id " + id + " does not exist"));
    }
}
