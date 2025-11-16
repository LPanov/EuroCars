package app.eurocars.category;

import app.eurocars.category.model.Category;
import app.eurocars.category.repository.CategoryRepository;
import app.eurocars.category.service.CategoryService;
import app.eurocars.exception.CategoryNotFound;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void whenGetAllMainCategories_thenReturnCategoriesWithParentNull(){
        Category category = Category.builder().imageUrl("imageUrl").build();

        when(categoryRepository.getCategoriesByImageUrlNotNull()).thenReturn(List.of(category));

        List<Category> mainCategories = categoryService.getAllMainCategories();

        assertThat(mainCategories.size()).isEqualTo(1);
    }

    @Test
    void whenGetAllSubCategories_thenReturnCategoriesWithoutImageAndParentNotNull(){
        Category category = Category.builder().imageUrl(null).build();

        when(categoryRepository.getCategoriesByImageUrlIsNull()).thenReturn(List.of(category));

        List<Category> subCategories = categoryService.getAllSubCategories();

        assertThat(subCategories.size()).isEqualTo(1);
    }

    @Test
    void whenGetAllCategories_thenReturnListOfAllCategories(){
        when(categoryRepository.findAll()).thenReturn(List.of(new Category(), new Category()));

        List<Category> subCategories = categoryService.getAllCategories();

        assertThat(subCategories.size()).isEqualTo(2);
    }

    @Test
    void givenExistingCategoryId_whenGetById_thenReturnCorrespondingCategory(){
        Long categoryId = 1L;
        Category category = Category.builder().id(categoryId).build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        Category foundCategory = categoryService.getById(categoryId);

        assertEquals(category.getId(), foundCategory.getId());
    }

    @Test
    void givenNonExistingCategoryId_whenGetById_thenThrowCategoryNotFound() {
        Long invalidId = 1000L;

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFound.class, () -> categoryService.getById(invalidId));
    }
}
