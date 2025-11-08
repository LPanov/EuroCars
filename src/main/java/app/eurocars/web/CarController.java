package app.eurocars.web;

import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.service.CartService;
import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final EngineService engineService;
    private final CategoryService categoryService;

    public CarController(EngineService engineService, CategoryService categoryService) {
        this.engineService = engineService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}/categories")
    public ModelAndView getVehicleManu(@PathVariable Long id) {
        Engine engine = engineService.getById(id);

        List<Category> mainCategories = categoryService.getAllMainCategories();
        List<Category> subCategories = categoryService.getAllSubCategories();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("car");
        modelAndView.addObject("mainCategories", mainCategories);
        modelAndView.addObject("subCategories", subCategories);
        modelAndView.addObject("engine", engine);

        return modelAndView;
    }

    @GetMapping("/0/categories")
    public ModelAndView getVehicleMenu(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        List<Category> mainCategories = categoryService.getAllMainCategories();
        List<Category> subCategories = categoryService.getAllSubCategories();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("car");
        modelAndView.addObject("mainCategories", mainCategories);
        modelAndView.addObject("subCategories", subCategories);
        modelAndView.addObject("engine", new Engine());

        return modelAndView;
    }
}
