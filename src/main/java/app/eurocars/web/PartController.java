package app.eurocars.web;

import app.eurocars.manufacturer.model.Manufacturer;
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
public class PartController {

    private final PartService partService;
    private final UserService userService;

    public PartController(PartService partService, UserService userService) {
        this.partService = partService;
        this.userService = userService;
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
}
