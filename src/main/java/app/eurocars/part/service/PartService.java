package app.eurocars.part.service;

import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.exception.DomainException;
import app.eurocars.part.model.Part;
import app.eurocars.part.repository.PartRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Part getPartById(String partId) {
        return partRepository.findPartById(UUID.fromString(partId)).orElseThrow(() -> new DomainException("Part not found")) ;
    }

    public List<Part> findCrossReferences(Part part) {
        List<Part> partsByCategory = partRepository.findPartsByCategory(part.getCategory());
        List<Part> filteredParts = new ArrayList<>();

        for (Part p : partsByCategory) {
            for (Engine e : p.getEngines()) {
                if (part.getEngines().stream().anyMatch(engine -> engine.getId().equals(e.getId()) && !part.getId().equals(p.getId())) ) {
                    filteredParts.add(p);
                }
            }
        }

        return filteredParts;
    }

    public void inflatePrices()  {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://tradingeconomics.com/bulgaria/inflation-cpi")
                    // Key addition: Pretend to be a common browser
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36")
                    .timeout(10000) // Increase timeout just in case
                    .get();
        } catch (IOException e) {
            System.err.println("Failed to connect to inflation source: " + e.getMessage());
        }

        double inflationRate = 0;

        if (doc != null) {
            inflationRate = Double.parseDouble(doc.select("tr.datatable-row-alternating > td:nth-child(2)").text().split("\\s+")[4]);
        }

        double rate = inflationRate/100 + 1;

        partRepository.updateAllPrices(rate);

        System.out.println("Prices updated at " + LocalDateTime.now());
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }
}
