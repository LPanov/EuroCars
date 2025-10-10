package app.eurocars.car.models.service;

import app.eurocars.car.models.model.Model;
import app.eurocars.car.models.repository.ModelRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModelService {

    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> findAllByBrand(String brandId) {
        List<Model> allModelsByBrand = modelRepository.findAll();

        if (brandId != null) {
            allModelsByBrand = allModelsByBrand.stream().filter(m -> m.getBrand().getId().equals(Long.parseLong(brandId))).toList();
        }

        return allModelsByBrand;
    }
}
