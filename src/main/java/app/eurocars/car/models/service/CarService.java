package app.eurocars.car.models.service;

import app.eurocars.car.models.repository.ModelRepository;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    private final ModelRepository modelRepository;

    public CarService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }
}
