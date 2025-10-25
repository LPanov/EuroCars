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
import app.eurocars.web.dto.AddPartRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PartController {

    private final PartService partService;
    private final UserService userService;
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;
    private final EngineService engineService;

    public PartController(PartService partService, UserService userService, ManufacturerService manufacturerService, CategoryService categoryService, EngineService engineService) {
        this.partService = partService;
        this.userService = userService;
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
        this.engineService = engineService;
    }

    @RequestMapping("/part")
    public ModelAndView getPartPage(
            @AuthenticationPrincipal AuthenticationDetails authenticationDetails,
            @RequestParam(value = "partId") String partId) {
        User user = userService.getById(authenticationDetails.getUserId());
        Part selectedPart = partService.getPartById(partId);
        List<Part> crossReferences = partService.findCrossReferences(selectedPart);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("part");
        modelAndView.addObject("user", user);
        modelAndView.addObject("part", selectedPart);
        modelAndView.addObject("crossReferences", crossReferences);

        return modelAndView;
    }

    @GetMapping("parts-settings/add-part")
    public ModelAndView getAddPartPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());
        List<Manufacturer> allManufacturers = manufacturerService.getAllManufacturers();
        List<Category> allCategories = categoryService.getAllCategories();
        List<Engine> allEngines = engineService.getAllEngines();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-part");
        modelAndView.addObject("user", user);
        modelAndView.addObject("createPart", new AddPartRequest());
        modelAndView.addObject("allManufacturers", allManufacturers);
        modelAndView.addObject("allCategories", allCategories);
        modelAndView.addObject("allEngines", allEngines);

        return modelAndView;
    }

    @PostMapping("parts-settings/add-part")
    public ModelAndView createNewPart(@Valid AddPartRequest createPart, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        partService.createPart(createPart);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/parts-settings/add-part");
        modelAndView.addObject("createPart", createPart);
        modelAndView.addObject("user", userService.getById(authenticationDetails.getUserId()));

        return modelAndView;
    }
}
