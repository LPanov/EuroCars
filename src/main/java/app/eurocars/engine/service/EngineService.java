package app.eurocars.engine.service;

import app.eurocars.car.models.model.Model;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.repository.EngineRepository;
import app.eurocars.exception.DomainException;
import app.eurocars.exception.EngineNotFound;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EngineService {

    private final EngineRepository engineRepository;

    public EngineService(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

    public Engine getById(Long id) {
        return engineRepository.findById(id).orElseThrow(() -> new EngineNotFound("Car with such engine id does not exist"));
    }

    public List<Engine> findAllByModel(String modelId) {
        List<Engine> allEnginesByModel = new ArrayList<>();

        if (modelId != null) {
            allEnginesByModel = engineRepository.findAllByModelId(Long.valueOf(modelId));
        }

        return allEnginesByModel;
    }

    @Cacheable("engines")
    public List<Engine> getAllEngines() {
        return engineRepository.findAll();
    }
}
