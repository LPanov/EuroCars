package app.eurocars.web;

import app.eurocars.brand.model.Brand;
import app.eurocars.brand.service.BrandService;
import app.eurocars.car.models.model.Model;
import app.eurocars.car.models.service.ModelService;
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
    private final UserService userService;
    private final ModelService modelService;
    private final EngineService engineService;


    public CatalogueController(BrandService brandService, UserService userService, ModelService modelService, EngineService engineService) {
        this.brandService = brandService;
        this.userService = userService;
        this.modelService = modelService;
        this.engineService = engineService;
    }

    @GetMapping("/brands")
    public ModelAndView getBrands(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getUserId());
        List<Brand> cars = brandService.findALlByType(VehicleType.builder().id(1L).build());
        List<Brand> vans = brandService.findALlByType(VehicleType.builder().id(2L).build());
        List<Brand> trucks = brandService.findALlByType(VehicleType.builder().id(3L).build());
        List<Brand> motorcycles = brandService.findALlByType(VehicleType.builder().id(4L).build());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("brands");
        modelAndView.addObject("user", user);
        modelAndView.addObject("cars", cars);
        modelAndView.addObject("vans", vans);
        modelAndView.addObject("trucks", trucks);
        modelAndView.addObject("motorcycles", motorcycles);

        return modelAndView;
    }

    @GetMapping("/models")
    public ModelAndView getModels(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                  @RequestParam String brandId) {

        User user = userService.getById(authenticationDetails.getUserId());
        List<Model> models = modelService.findAllByBrand(brandId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("models");
        modelAndView.addObject("user", user);
        modelAndView.addObject("models", models);

        return modelAndView;
    }

    @GetMapping("/engines")
    public ModelAndView getEngines(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                  @RequestParam String modelId) {

        User user = userService.getById(authenticationDetails.getUserId());
        List<Engine> engines = engineService.findAllByModel(modelId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("engines");
        modelAndView.addObject("user", user);
        modelAndView.addObject("engines", engines);

        return modelAndView;
    }
}
