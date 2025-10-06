package app.eurocars.part.service;

import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.exception.DomainException;
import app.eurocars.part.model.Part;
import app.eurocars.part.repository.PartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {

    private final PartRepository partRepository;
    private final EngineService engineService;
    private final CategoryService categoryService;

    public PartService(PartRepository partRepository, EngineService engineService, CategoryService categoryService) {
        this.partRepository = partRepository;
        this.engineService = engineService;
        this.categoryService = categoryService;
    }

    public List<Part> getAllFilteredParts(String vehicleId, String categoryId, String input) {
        List<Part> allParts = partRepository.findAll();

        if (vehicleId != null) {
            Engine selectedEngine = engineService.findById(vehicleId);
            allParts = allParts.stream().filter(p -> p.getEngines().stream().anyMatch(e -> e.getId().equals(selectedEngine.getId()))).toList();
        }

        if (categoryId != null) {
            Category selectedCategory = categoryService.getById(Long.valueOf(categoryId));
            allParts = allParts.stream().filter(p -> p.getCategory().getId().equals(selectedCategory.getId())).toList();
        }

        if (input != null) {
            allParts = partRepository.findPartsByInput(input);
        }

        return allParts;
    }

    public void save(Part part) {
        partRepository.save(part);
    }
}
