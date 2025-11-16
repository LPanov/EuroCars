package app.eurocars.part.service;

import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.exception.DomainException;
import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.model.Part;
import app.eurocars.part.repository.PartRepository;
import app.eurocars.web.dto.AddPartRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PartService {

    private final PartRepository partRepository;
    private final EngineService engineService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;

    public PartService(PartRepository partRepository, EngineService engineService, CategoryService categoryService, ManufacturerService manufacturerService) {
        this.partRepository = partRepository;
        this.engineService = engineService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
    }

    public List<Part> getAllFilteredParts(String vehicleId, String categoryId, String input) {
        List<Part> allParts = getAllParts();

        if (vehicleId != null) {
            Engine selectedEngine = engineService.getById(Long.valueOf(vehicleId));
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

    public void createPart(AddPartRequest createPart) {
        Set<Engine> engines = new HashSet<>();

        Category category = categoryService.getById(createPart.getCategory());
        Engine engine = engineService.getById(createPart.getEngine());
        Manufacturer manufacturer = manufacturerService.findById(createPart.getManufacturer());

        engines.add(engine);
        Set<String> imgUl = getUrls(createPart);

        Set<String> otherNumbers = getOtherNumbers(createPart);


        Part part = Part.builder()
                .name(createPart.getName())
                .description(createPart.getDescription())
                .weight(createPart.getWeight())
                .price(createPart.getPrice())
                .additionalInformation(createPart.getAdditionalInformation())
                .manufacturer(manufacturer)
                .category(category)
                .engines(engines)
                .imgUrls(imgUl)
                .otherNumbers(otherNumbers)
                .createdDate(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        partRepository.save(part);
    }

    public void updatePart(AddPartRequest editPart, String partId) {
        Part part = getPartById(partId);

        Category category = categoryService.getById(editPart.getCategory());
        Engine engine = engineService.getById(editPart.getEngine());
        Manufacturer manufacturer = manufacturerService.findById(editPart.getManufacturer());

        Set<String> imgUrls = getUrls(editPart);

        Set<String> otherNumbers = getOtherNumbers(editPart);

        part.setName(editPart.getName());
        part.setDescription(editPart.getDescription());
        part.setWeight(editPart.getWeight());
        part.setPrice(editPart.getPrice());
        part.setAdditionalInformation(editPart.getAdditionalInformation());
        part.setManufacturer(manufacturer);
        part.setCategory(category);
        part.getEngines().add(engine);
        if (!imgUrls.isEmpty()) {
            imgUrls.forEach(imgUrl -> part.getImgUrls().add(imgUrl));
        }
        if (!otherNumbers.isEmpty()) {
            otherNumbers.forEach(otherNumber -> part.getOtherNumbers().add(otherNumber));
        }
        part.setUpdatedOn(LocalDateTime.now());

        partRepository.save(part);
    }

    private static Set<String> getOtherNumbers(AddPartRequest editPart) {
        Set<String> otherNumbers = new HashSet<>();
        if (editPart.getOtherNumbers() != null && !editPart.getOtherNumbers().isEmpty()) {
            otherNumbers = Arrays.stream(editPart.getOtherNumbers().split("\n")).collect(Collectors.toSet());
        }
        return otherNumbers;
    }

    private static Set<String> getUrls(AddPartRequest editPart) {
        Set<String> imgUrls = new HashSet<>();
        if (editPart.getImgUrls() != null && !editPart.getImgUrls().isEmpty() ) {
            imgUrls = Arrays.stream(editPart.getImgUrls().split("\n")).collect(Collectors.toSet());
        }
        return imgUrls;
    }

    public void deletePartById(UUID partId) {
        partRepository.deleteById(partId);
    }
}
