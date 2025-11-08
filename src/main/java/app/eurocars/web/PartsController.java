package app.eurocars.web;

import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PartsController {

    private final PartService partService;
    private final ManufacturerService manufacturerService;


    public PartsController(PartService partService, ManufacturerService manufacturerService) {
        this.partService = partService;
        this.manufacturerService = manufacturerService;
    }

    @RequestMapping("/parts")
    @GetMapping
    public ModelAndView getPartsPage(
            @RequestParam(value = "vehicleId", required = false) String vehicleId,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "input", required = false) String input) {
        List<Manufacturer> allManufacturers = manufacturerService.getAllManufacturers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("list-parts");
        modelAndView.addObject("manufacturers", allManufacturers);

        List<Part> allParts = partService.getAllFilteredParts(vehicleId, categoryId, input);

        modelAndView.addObject("parts", allParts);

        return modelAndView;
    }

    @RequestMapping("/parts-settings")
    public ModelAndView getPartsSettingsPage() {
        List<Part> allParts = partService.getAllParts();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("parts");
        modelAndView.addObject("parts", allParts);

        return modelAndView;
    }
}
