package app.eurocars.car.model;

import app.eurocars.brand.model.Brand;
import app.eurocars.car.models.model.Model;
import app.eurocars.car.models.repository.ModelRepository;
import app.eurocars.car.models.service.ModelService;
import app.eurocars.exception.ModelNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ModelServiceUTest {

    @Mock
    private ModelRepository modelRepository;

    @InjectMocks
    private ModelService modelService;

    @Test
    void givenBrandId_whenFindAllByBrand_thenFilteredListByBrandIsReturned() {
        // Given
        Brand targetBrand = Brand.builder().id(1L).build();
        Model model1 = Model.builder().brand(targetBrand).build();
        Model model2 = Model.builder().brand(Brand.builder().id(2L).build()).build();

        when(modelRepository.findAll()).thenReturn(List.of(model1, model2));
        // When
        List<Model> filteredModels = modelService.findAllByBrand("1");

        // Then
        assertEquals(1, filteredModels.size());
        verify(modelRepository).findAll();
    }

    @Test
    void givenBrandIdIsNull_whenFindAllByBrand_thenReturnEmptyListAndSkipRepositoryCall() {
        List<Model> filteredModels = modelService.findAllByBrand(null);

        assertTrue(filteredModels.isEmpty());
        verify(modelRepository, never()).findAll();
    }


    @Test
    void givenModelId_whenGetById_thenReturnModelWithSuchId() {
        Model model = Model.builder().id(1L).build();

        when(modelRepository.findModelById(any())).thenReturn(Optional.of(model));

        Model foundModel = modelService.getById(anyLong());

        assertEquals(model.getId(), foundModel.getId());
    }

    @Test
    void givenNonExistingModelId_whenGetById_thenThrowModelNotFound() {
        Long modelId = 1000L;

        when(modelRepository.findModelById(any())).thenReturn(Optional.empty());

        assertThrows(ModelNotFound.class, () -> modelService.getById(modelId));
    }

}


