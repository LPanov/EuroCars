package app.eurocars.web;

import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/account-settings")
    public ModelAndView getProfileMenu(@PathVariable UUID id) {
        User user = userService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("account-settings");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping
    public ModelAndView getUsersPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());
        List<User> allUsers = userService.getAllNonAdminUsers();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", allUsers);

        return modelAndView;
    }

    @DeleteMapping
    public ModelAndView deleteUser(@RequestParam UUID userId) {
        userService.deleteUserById(userId);

        return new ModelAndView("redirect:/users");
    }
}
