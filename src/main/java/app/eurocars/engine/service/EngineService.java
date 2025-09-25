package app.eurocars.engine.service;

import app.eurocars.engine.model.Engine;
import app.eurocars.engine.repository.EngineRepository;
import app.eurocars.exception.DomainException;
import org.springframework.stereotype.Service;

@Service
public class EngineService {

    private final EngineRepository engineRepository;

    public EngineService(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

    public Engine getById(Long id) {
        return engineRepository.findById(id).orElseThrow(() -> new DomainException("Car with such engine id does not exist"));
    }
}
