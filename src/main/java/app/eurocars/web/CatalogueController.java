package app.eurocars.web;

import app.eurocars.brand.service.BrandService;
import app.eurocars.car.models.service.ModelService;
import app.eurocars.engine.service.EngineService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.vehicle.type.model.VehicleType;
import app.eurocars.vehicle.type.service.VehicleTypeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CatalogueController {

    private final VehicleTypeService vehicleTypeService;
    private final BrandService brandService;
    private final EngineService engineService;
    private final ModelService modelService;
    private final UserService userService;


    public CatalogueController(VehicleTypeService vehicleTypeService, BrandService brandService, EngineService engineService, ModelService modelService, UserService userService) {
        this.vehicleTypeService = vehicleTypeService;
        this.brandService = brandService;
        this.engineService = engineService;
        this.modelService = modelService;
        this.userService = userService;
    }

    @GetMapping("/catalogue")
    public ModelAndView getCatalogue(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        User user = userService.getById(authenticationDetails.getUserId());
        VehicleType selectedVehicleType = vehicleTypeService.getSelectedVehicleType();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("choose-model");
        modelAndView.addObject("user", user);
        modelAndView.addObject("vehicleType", selectedVehicleType);

        System.out.println("Act 2 - " + selectedVehicleType.getName());

        return modelAndView;
    }
}
