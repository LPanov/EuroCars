package app.eurocars.car.models.service;

import app.eurocars.car.models.model.Model;
import app.eurocars.car.models.repository.ModelRepository;
import app.eurocars.exception.ModelNotFound;
import org.springframework.cache.annotation.Cacheable;
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
        List<Model> allModelsByBrand = new ArrayList<>();

        if (brandId != null) {
            allModelsByBrand = getAll().stream().filter(m -> m.getBrand().getId().equals(Long.parseLong(brandId))).toList();
        }

        return allModelsByBrand;
    }

    public Model getById(Long modelId) {
        return modelRepository.findModelById(modelId).orElseThrow(() -> new ModelNotFound("Model with such id does not exist"));
    }

    @Cacheable("models")
    public List<Model> getAll() {
        return modelRepository.findAll();
    }
}
