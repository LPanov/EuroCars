package app.eurocars.web;

import app.eurocars.cart.client.dto.CartItemResponse;
import app.eurocars.cart.service.CartService;
import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.part.service.PartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.web.dto.CartItemRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final PartService partService;
    private final UserService userService;

    public CartController(CartService cartService, PartService partService, UserService userService) {
        this.cartService = cartService;
        this.partService = partService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getCartPage(@AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        User user = userService.getById(authenticationDetails.getUserId());
        List<CartItem> items = cartService.getCartItemsByUserId(user.getId());
        BigDecimal totalPriceWithVat = CartService.getTotalPriceWithVat(items);
        double totalWeight = CartService.getTotalWeight(items);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cart");
        modelAndView.addObject("user", user);
        modelAndView.addObject("items", items);
        modelAndView.addObject("totalPriceWithVat", totalPriceWithVat);
        modelAndView.addObject("totalWeight", totalWeight);

        return modelAndView;
    }

    @PostMapping
    public ModelAndView addToCart(CartItemRequest cartItemRequest, @AuthenticationPrincipal AuthenticationDetails authenticationDetails) {
        if (cartItemRequest.getPartId() != null) {
            User user = userService.getById(authenticationDetails.getUserId());

//            cartService.addToCart(cartItemRequest, user);

            return new ModelAndView("redirect:/part?partId=" + cartItemRequest.getPartId());
        }

        return new ModelAndView("redirect:/home");
    }

    @DeleteMapping
    public ModelAndView deleteCartItem(@RequestParam Long itemId) {
//        cartItemService.deleteCartItem(itemId);

        return new ModelAndView("redirect:/cart");
    }
}
