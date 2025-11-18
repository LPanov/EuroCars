package app.eurocars.part.service;

import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.service.EngineService;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.web.dto.AddPartRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PartInit implements CommandLineRunner {

    private final PartService partService;
    private final CategoryService categoryService;
    private final ManufacturerService manufacturerService;
    private final EngineService engineService;

    public PartInit(PartService partService, CategoryService categoryService, ManufacturerService manufacturerService, EngineService engineService) {
        this.partService = partService;
        this.categoryService = categoryService;
        this.manufacturerService = manufacturerService;
        this.engineService = engineService;
    }


    @Override
    public void run(String... args) throws Exception {
        if (!partService.getAllParts().isEmpty() &&
                categoryService.getAllCategories().isEmpty() &&
                manufacturerService.getAllManufacturers().isEmpty() &&
                engineService.getAllEngines().isEmpty()) {
            return;
        }

        AddPartRequest addPartRequest = AddPartRequest.builder()
                .name("0 451 103 314")
                .description("Oil filter")
                .additionalInformation("Oil filter fits: MERCEDES 190 (W201); AUDI 100 C4, 200 C3, 80 B3, 80 B4, A3, A4 B5, A4 B6, A4 B7, A6 C4, A6 C5, CABRIOLET B3, COUPE B2, COUPE B3, QUATTRO, TT; SEAT ALHAMBRA 1.0-4.2 04.84-12.17")
                .weight(0.38)
                .price(BigDecimal.valueOf(6.58))
                .category(19L)
                .manufacturer(35L)
                .imgUrls("https://i.ibb.co/PzMrk3BZ/image.png\nhttps://i.ibb.co/wNLLBVJS/image.png\nhttps://i.ibb.co/ns4RzDb7/image.png")
                .otherNumbers("0451103070\nSP-978\nB003FSUYSM\n0 451 103 302\nSP-1137")
                .engine(1L)
                .build();

        partService.createPart(addPartRequest);
    }
}
