package app.eurocars.web;

import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PartsController {

    private final UserService userService;
    private final PartService partService;
    private final ManufacturerService manufacturerService;


    public PartsController(UserService userService, PartService partService, ManufacturerService manufacturerService) {
        this.userService = userService;
        this.partService = partService;
        this.manufacturerService = manufacturerService;
    }

    @RequestMapping("/parts")
    public ModelAndView getPartsPage(
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails,
            @RequestParam(value = "vehicleId", required = false) String vehicleId,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "input", required = false) String input) {
        User user = userService.getById(authenticationDetails.getUserId());
        List<Manufacturer> allManufacturers = manufacturerService.getAllManufacturers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list-parts");
        modelAndView.addObject("user", user);
        modelAndView.addObject("manufacturers", allManufacturers);

        List<Part> allParts = partService.getAllFilteredParts(vehicleId, categoryId, input);

        modelAndView.addObject("parts", allParts);

        return modelAndView;
    }

    @RequestMapping("/parts-settings")
    public ModelAndView getPartsSettingsPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());
        List<Part> allParts = partService.getAllParts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parts");
        modelAndView.addObject("user", user);
        modelAndView.addObject("parts", allParts);

        return modelAndView;
    }
}
