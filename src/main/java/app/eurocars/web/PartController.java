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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("parts-settings/new-part")
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

    @PostMapping("parts-settings/new-part")
    public ModelAndView createNewPart(@Valid AddPartRequest createPart, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {

        partService.createPart(createPart);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/parts-settings/add-part");
        modelAndView.addObject("createPart", createPart);
        modelAndView.addObject("user", userService.getById(authenticationDetails.getUserId()));

        return modelAndView;
    }

    @GetMapping("/parts-settings/{partId}")
    public ModelAndView getEditPartPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                        @PathVariable String partId) {
        User user = userService.getById(authenticationDetails.getUserId());
        Part part = partService.getPartById(partId);
        List<Manufacturer> allManufacturers = manufacturerService.getAllManufacturers();
        List<Category> allCategories = categoryService.getAllCategories();
        List<Engine> allEngines = engineService.getAllEngines();
        AddPartRequest editPart = AddPartRequest.builder()
                .id(part.getId())
                .name(part.getName())
                .description(part.getDescription())
                .additionalInformation(part.getAdditionalInformation())
                .weight(part.getWeight())
                .price(part.getPrice())
                .category(part.getCategory().getId())
                .manufacturer(part.getManufacturer().getId())
                .engine(part.getEngines().stream().findFirst().get().getId())
                .build();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit-part");
        modelAndView.addObject("user", user);
        modelAndView.addObject("part", part);
        modelAndView.addObject("editPart", editPart);
        modelAndView.addObject("allManufacturers", allManufacturers);
        modelAndView.addObject("allCategories", allCategories);
        modelAndView.addObject("allEngines", allEngines);

        return modelAndView;
    }

    @PutMapping("/parts-settings/{partId}")
    public ModelAndView editPart(@Valid AddPartRequest editPart,
                                 @AuthenticationPrincipal AuthenticationDetails authenticationDetails,
                                 @PathVariable String partId) {

        partService.updatePart(editPart, partId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/parts-settings/"+partId);
        modelAndView.addObject("editPart", editPart);
        modelAndView.addObject("user", userService.getById(authenticationDetails.getUserId()));

        return modelAndView;
    }

    @DeleteMapping("/parts-settings")
    public ModelAndView deletePart(@RequestParam UUID partId) {
        partService.deletePartById(partId);

        return new ModelAndView("redirect:/parts-settings");
    }
}
