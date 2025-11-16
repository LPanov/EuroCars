package app.eurocars.engine;

import app.eurocars.brand.model.Brand;
import app.eurocars.car.models.model.Model;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.repository.EngineRepository;
import app.eurocars.engine.service.EngineService;
import app.eurocars.exception.EngineNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EngineServiceUTest {
    @Mock
    private EngineRepository engineRepository;

    @InjectMocks
    private EngineService engineService;

    @Test
    void givenExistingEngineId_whenGetById_thenReturnCorrespondingEngine(){
        Long engineId = 1L;
        Engine engine = Engine.builder().id(engineId).build();

        when(engineRepository.findById(engineId)).thenReturn(Optional.of(engine));

        Engine foundEngine = engineService.getById(engineId);

        assertEquals(engine.getId(), foundEngine.getId());
    }

    @Test
    void givenNonExistingEngineId_whenGetById_thenThrowEngineNotFound() {
        when(engineRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EngineNotFound.class, () -> engineService.getById(anyLong()));
    }

    @Test
    void givenModelId_whenFindAllByModel_thenFilteredListByModelIsReturned() {
        Long modelId = 1L;

        Model targetedModel = Model.builder().id(modelId).build();
        Engine engine1 = Engine.builder().model(targetedModel).build();

        when(engineRepository.findAllByModelId(modelId)).thenReturn(List.of(engine1));

        List<Engine> filteredEngines = engineService.findAllByModel(String.valueOf(modelId));

        assertEquals(1, filteredEngines.size());
    }

    @Test
    void givenModelIdIsNull_whenFindAllByModel_thenReturnEmptyListAndSkipRepositoryCall() {
        List<Engine> filteredEngines = engineService.findAllByModel(null);

        assertTrue(filteredEngines.isEmpty());
    }

    @Test
    void whenGetAllEngines_thenReturnListWithAllEngines() {
        when(engineRepository.findAll()).thenReturn(List.of(new Engine(),  new Engine()));

        List<Engine> engines = engineService.getAllEngines();

        assertThat(engines.size()).isEqualTo(2);
    }

}
