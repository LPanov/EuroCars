package app.eurocars.manufacturer.service;

import app.eurocars.exception.DomainException;
import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.repository.ManufacturerRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerService(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    @Cacheable("manufacturers")
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    public Manufacturer findById(Long manufacturer) {
        return manufacturerRepository.findById(manufacturer).orElseThrow(() -> new DomainException("Manufacturer with such id not found"));
    }
}
