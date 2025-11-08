package app.eurocars.web;

import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.service.CartService;
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
import app.eurocars.cart.client.dto.CartItemRequest;
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
    private final ManufacturerService manufacturerService;
    private final CategoryService categoryService;
    private final EngineService engineService;

    public PartController(PartService partService, ManufacturerService manufacturerService, CategoryService categoryService, EngineService engineService) {
        this.partService = partService;
        this.manufacturerService = manufacturerService;
        this.categoryService = categoryService;
        this.engineService = engineService;
    }

    @RequestMapping("/part")
    @GetMapping
    public ModelAndView getPartPage(@RequestParam(value = "partId") String partId) {
        Part selectedPart = partService.getPartById(partId);
        List<Part> crossReferences = partService.findCrossReferences(selectedPart);
        CartItemRequest cartItemRequest = CartItemRequest.builder().partId(selectedPart.getId()).build();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("part");
        modelAndView.addObject("part", selectedPart);
        modelAndView.addObject("crossReferences", crossReferences);
        modelAndView.addObject("cartItemRequest", cartItemRequest);

        return modelAndView;
    }

    @GetMapping("parts-settings/new-part")
    public ModelAndView getAddPartPage() {
        List<Manufacturer> allManufacturers = manufacturerService.getAllManufacturers();
        List<Category> allCategories = categoryService.getAllCategories();
        List<Engine> allEngines = engineService.getAllEngines();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add-part");
        modelAndView.addObject("createPart", new AddPartRequest());
        modelAndView.addObject("allManufacturers", allManufacturers);
        modelAndView.addObject("allCategories", allCategories);
        modelAndView.addObject("allEngines", allEngines);

        return modelAndView;
    }

    @PostMapping("parts-settings/new-part")
    public ModelAndView createNewPart(@Valid AddPartRequest createPart) {

        partService.createPart(createPart);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/parts-settings/add-part");
        modelAndView.addObject("createPart", createPart);

        return modelAndView;
    }

    @GetMapping("/parts-settings/{partId}")
    public ModelAndView getEditPartPage(@PathVariable String partId) {
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
        modelAndView.addObject("part", part);
        modelAndView.addObject("editPart", editPart);
        modelAndView.addObject("allManufacturers", allManufacturers);
        modelAndView.addObject("allCategories", allCategories);
        modelAndView.addObject("allEngines", allEngines);

        return modelAndView;
    }

    @PutMapping("/parts-settings/{partId}")
    public ModelAndView editPart(@Valid AddPartRequest editPart,
                                 @PathVariable String partId) {

        partService.updatePart(editPart, partId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/parts-settings/"+partId);
        modelAndView.addObject("editPart", editPart);

        return modelAndView;
    }

    @DeleteMapping("/parts-settings")
    public ModelAndView deletePart(@RequestParam UUID partId) {
        partService.deletePartById(partId);

        return new ModelAndView("redirect:/parts-settings");
    }
}
