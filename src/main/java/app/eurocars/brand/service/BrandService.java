package app.eurocars.brand.service;

import app.eurocars.brand.model.Brand;
import app.eurocars.brand.repository.BrandRepository;
import app.eurocars.vehicle.type.model.VehicleType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {


    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Cacheable("brands")
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    public List<Brand> findALlByType(VehicleType vehicleType) {
        return findAll().stream().filter(b -> b.getTypes().stream().anyMatch(type -> type.getId().equals(vehicleType.getId()))).toList();
    }
}
