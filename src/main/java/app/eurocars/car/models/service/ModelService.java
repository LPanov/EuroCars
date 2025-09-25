package app.eurocars.car.models.service;

import app.eurocars.car.models.repository.ModelRepository;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    private final ModelRepository modelRepository;

    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }
}
