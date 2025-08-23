package app.eurocars.part.service;

import app.eurocars.part.repository.PartRepository;
import org.springframework.stereotype.Service;

@Service
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }
}
