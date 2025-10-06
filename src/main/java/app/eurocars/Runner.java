package app.eurocars;

import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.repository.EngineRepository;
import app.eurocars.manufacturer.repository.ManufacturerRepository;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class Runner implements CommandLineRunner {

    private final PartService partService;
    private final CategoryService categoryService;
    private final ManufacturerRepository manufacturerService;
    private final EngineRepository engineRepository;


    public Runner(PartService partService, CategoryService categoryService, ManufacturerRepository manufacturerService, EngineRepository engineRepository) {
        this.partService = partService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
        this.engineRepository = engineRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        Part part = new Part();
//        Category category = categoryService.findById(32L);
//        part.setCategory(category);
//        part.getEngines().add(engineRepository.findById(1L).get());
//        part.setDescription("Clutch Kit");
//        part.setName("VAL786028");
//        part.setPrice(BigDecimal.valueOf(225.84));
//        part.setAdditionalInformation("Clutch kit (210mm) fits: AUDI A3; SEAT CORDOBA, CORDOBA VARIO, IBIZA II, LEON, TOLEDO I, TOLEDO II; SKODA OCTAVIA I; VW BORA, BORA I, CORRADO, GOLF III, GOLF IV, POLO 1.6/1.8/2.0 05.91-12.10");
//        part.getOtherNumbers().add("B00CW001PU");
//        part.getOtherNumbers().add("786028");
//        part.getOtherNumbers().add("HK06484");
//        part.getOtherNumbers().add("06A 141 025 J");
//        part.setWeight(5.46);
//        part.getImgUrls().add("https://i.ibb.co/ZpPwfLqM/image.png");
//        part.getImgUrls().add("https://i.ibb.co/tpk1mW0w/image.png");
//        part.getImgUrls().add("https://i.ibb.co/mVRnqkJZ/image.png");
//        part.setManufacturer(manufacturerService.getById(31L));
//        part.setCreatedDate(LocalDateTime.now());
//        part.setUpdatedOn(LocalDateTime.now());
//
//        partService.save(part);
//        System.out.println(part.getName());

    }
}
