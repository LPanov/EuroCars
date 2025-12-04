package app.eurocars.scheduler;

import app.eurocars.brand.service.BrandService;
import app.eurocars.car.models.service.ModelService;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.service.EngineService;
import app.eurocars.manufacturer.service.ManufacturerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheRefreshScheduler {

    private final CategoryService categoryService;
    private final EngineService engineService;
    private final ManufacturerService manufacturerService;
    private final BrandService brandService;
    private final ModelService modelService;

    public CacheRefreshScheduler(
            CategoryService categoryService,
            EngineService engineService,
            ManufacturerService manufacturerService, BrandService brandService, ModelService modelService) {
        this.categoryService = categoryService;
        this.engineService = engineService;
        this.manufacturerService = manufacturerService;
        this.brandService = brandService;
        this.modelService = modelService;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 12)
    @CacheEvict(cacheNames = {"categories", "engines", "manufacturers", "brands", "models"}, allEntries = true)
    public void refreshDomainCaches() {
        categoryService.getAllCategories();
        engineService.getAllEngines();
        manufacturerService.getAllManufacturers();
        brandService.findAll();
        modelService.getAll();
    }
}
