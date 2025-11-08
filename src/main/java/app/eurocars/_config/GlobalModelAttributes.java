package app.eurocars._config;

import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.service.CartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.web.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.util.List;

@ControllerAdvice(assignableTypes = {CarController.class, CatalogueController.class, CarController.class, PartController.class, UserController.class, PartsController.class})
public class GlobalModelAttributes {
    private final UserService userService;
    private final CartService cartService;

    public GlobalModelAttributes(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @ModelAttribute
    public void addUserAndCart(Model model,
                               @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());

        if (user != null) {
            List<CartItem> items = cartService.getCartItemsByUserId(user.getId());
            BigDecimal wholePrice = cartService.getWholePrice(items);

            model.addAttribute("user", user);
            model.addAttribute("items", items);
            model.addAttribute("wholePrice", wholePrice);
        }
    }
}
