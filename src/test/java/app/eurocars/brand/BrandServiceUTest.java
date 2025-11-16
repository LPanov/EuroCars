package app.eurocars.brand;

import app.eurocars.brand.model.Brand;
import app.eurocars.brand.repository.BrandRepository;
import app.eurocars.brand.service.BrandService;
import app.eurocars.vehicle.type.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BrandServiceUTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @Test
    void givenExistingVehicleTypeInDatabase_whenFindAllByType_thenReturnAllBrandsFilteredByGivenType() {
        // Given
        VehicleType vehicleType = VehicleType.builder()
                                    .id(1L)
                                    .build();
        Brand brand1 = Brand.builder().types(List.of(vehicleType)).build();
        Brand brand2 = Brand.builder().types(new ArrayList<>()).build();
        List<Brand> brandList = List.of(brand1, brand2);

        when(brandRepository.findAll()).thenReturn(brandList);

        // When
        List<Brand> filteredBrands = brandService.findALlByType(vehicleType);

        // Then
        assertThat(filteredBrands).hasSize(1);
        assertThat(brandList).hasSize(2);
    }
}
