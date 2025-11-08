package app.eurocars.web;

import app.eurocars.brand.model.Brand;
import app.eurocars.brand.service.BrandService;
import app.eurocars.car.models.model.Model;
import app.eurocars.car.models.service.ModelService;
import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.service.CartService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.vehicle.type.model.VehicleType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CatalogueController {


    private final BrandService brandService;
    private final ModelService modelService;
    private final EngineService engineService;


    public CatalogueController(BrandService brandService, ModelService modelService, EngineService engineService) {
        this.brandService = brandService;
        this.modelService = modelService;
        this.engineService = engineService;
    }

    @GetMapping("/brands")
    public ModelAndView getBrands() {
        List<Brand> cars = brandService.findALlByType(VehicleType.builder().id(1L).build());
        List<Brand> vans = brandService.findALlByType(VehicleType.builder().id(2L).build());
        List<Brand> trucks = brandService.findALlByType(VehicleType.builder().id(3L).build());
        List<Brand> motorcycles = brandService.findALlByType(VehicleType.builder().id(4L).build());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("brands");
        modelAndView.addObject("cars", cars);
        modelAndView.addObject("vans", vans);
        modelAndView.addObject("trucks", trucks);
        modelAndView.addObject("motorcycles", motorcycles);

        return modelAndView;
    }

    @GetMapping("/models")
    public ModelAndView getModels(@RequestParam String brandId) {

        List<Model> models = modelService.findAllByBrand(brandId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("models");
        modelAndView.addObject("models", models);

        return modelAndView;
    }

    @GetMapping("/engines")
    public ModelAndView getEngines(@RequestParam String modelId) {
        List<Engine> engines = engineService.findAllByModel(modelId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("engines");
        modelAndView.addObject("engines", engines);

        return modelAndView;
    }
}
