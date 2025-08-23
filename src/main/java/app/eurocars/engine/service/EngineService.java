package app.eurocars.engine.service;

import app.eurocars.engine.repository.EngineRepository;
import org.springframework.stereotype.Service;

@Service
public class EngineService {

    private final EngineRepository engineRepository;

    public EngineService(EngineRepository engineRepository) {
        this.engineRepository = engineRepository;
    }

}
