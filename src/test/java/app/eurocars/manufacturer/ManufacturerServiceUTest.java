package app.eurocars.manufacturer;

import app.eurocars.engine.model.Engine;
import app.eurocars.exception.DomainException;
import app.eurocars.exception.EngineNotFound;
import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.repository.ManufacturerRepository;
import app.eurocars.manufacturer.service.ManufacturerService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceUTest {

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ManufacturerService manufacturerService;

    @Test
    void givenExistingManufacturerId_whenFindById_thenReturnCorrespondingManufacturer(){
        Long manufacturerId = 1L;
        Manufacturer manufacturer = Manufacturer.builder().id(manufacturerId).build();

        when(manufacturerRepository.findById(manufacturerId)).thenReturn(Optional.of(manufacturer));

        Manufacturer foundManufacturer = manufacturerService.findById(manufacturerId);

        assertEquals(manufacturer.getId(), foundManufacturer.getId());
    }

    @Test
    void givenNonExistingManufacturerId_whenFindById_thenThrowDomainException() {
        when(manufacturerRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> manufacturerService.findById(anyLong()));
    }

    @Test
    void whenGetAllManufacturers_thenReturnListWithAllManufacturers() {
        when(manufacturerRepository.findAll()).thenReturn(List.of(new Manufacturer(),  new Manufacturer()));

        List<Manufacturer> manufacturers = manufacturerService.getAllManufacturers();

        assertThat(manufacturers.size()).isEqualTo(2);
    }

}
