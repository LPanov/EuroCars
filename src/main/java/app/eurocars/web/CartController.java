package app.eurocars.web;

import app.eurocars.cart.service.CartService;
import app.eurocars.cartItem.model.CartItem;
import app.eurocars.cartItem.service.CartItemService;
import app.eurocars.part.service.PartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.web.dto.CartItemRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final PartService partService;
    private final UserService userService;
    private final CartItemService cartItemService;

    public CartController(CartService cartService, PartService partService, UserService userService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.partService = partService;
        this.userService = userService;
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ModelAndView getCartPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());
        List<CartItem> items = cartService.getCartByUser(user).getItems();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart");
        modelAndView.addObject("user", user);
        modelAndView.addObject("items", items);

        return modelAndView;
    }

    @PostMapping
    public ModelAndView addToCart(CartItemRequest cartItemRequest, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if (cartItemRequest.getPartId() != null) {
            User user = userService.getById(authenticationDetails.getUserId());

            cartService.addToCart(cartItemRequest, user);

            return new ModelAndView("redirect:/part?partId=" + cartItemRequest.getPartId());
        }

        return new ModelAndView("redirect:/home");
    }

    @DeleteMapping
    public ModelAndView deleteCartItem(@RequestParam Long itemId) {
        cartItemService.deleteCartItem(itemId);

        return new ModelAndView("redirect:/cart");
    }
}
