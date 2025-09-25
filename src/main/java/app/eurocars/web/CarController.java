package app.eurocars.web;

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

    private final UserService userService;
    private final EngineService engineService;
    private final CategoryService categoryService;

    public CarController(UserService userService, EngineService engineService, CategoryService categoryService) {
        this.userService = userService;
        this.engineService = engineService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}/categories")
    public ModelAndView getProfileMenu(@AuthenticationPrincipal AuthenticationDetails authenticationDetails, @PathVariable Long id) {
        Engine engine = engineService.getById(id);
        User user = userService.getById(authenticationDetails.getUserId());

        List<Category> mainCategories = categoryService.getAllMainCategories();
        List<Category> subCategories = categoryService.getAllSubCategories();

        //List<Category> categories = categoryService.getAllCategories();

//        Map<Category, List<Category>> subcategoriesByParent = subCategories.stream()
//                .filter(c -> c.getCategory() != null)
//                .collect(Collectors.groupingBy(Category::getCategory));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("car");
        modelAndView.addObject("user", user);
        modelAndView.addObject("mainCategories", mainCategories);
        modelAndView.addObject("subCategories", subCategories);
        modelAndView.addObject("engine", engine);

        return modelAndView;
    }
}
