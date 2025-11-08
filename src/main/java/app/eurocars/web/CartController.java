package app.eurocars.web;

import app.eurocars.cart.client.dto.CartItemResponse;
import app.eurocars.cart.service.CartService;
import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.part.service.PartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.cart.client.dto.CartItemRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getCartPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());
        List<CartItem> items = cartService.getCartItemsByUserId(user.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart");
        modelAndView.addObject("user", user);
        modelAndView.addObject("items", items);
        modelAndView.addObject("wholePrice", cartService.getWholePrice(items));

        return modelAndView;
    }

    @PostMapping("/item")
    public ModelAndView addToCart(@Valid CartItemRequest cartItemRequest, BindingResult bindingResult, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if (!bindingResult.hasErrors()) {
            User user = userService.getById(authenticationDetails.getUserId());
            cartItemRequest.setUserId(user.getId());

            cartService.addToCart(cartItemRequest);

            return new ModelAndView("redirect:/part?partId=" + cartItemRequest.getPartId());
        }

        return new ModelAndView("redirect:/home");
    }

    @DeleteMapping("/item")
    public ModelAndView deleteCartItem(@RequestParam UUID itemId) {
        cartService.removeFromCart(itemId);

        return new ModelAndView("redirect:/cart");
    }
}
