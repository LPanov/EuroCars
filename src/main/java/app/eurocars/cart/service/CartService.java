package app.eurocars.cart.service;

import app.eurocars.cart.model.Cart;
import app.eurocars.cart.repository.CartRepository;
import app.eurocars.cartItem.model.CartItem;
import app.eurocars.cartItem.service.CartItemService;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import app.eurocars.user.model.User;
import app.eurocars.web.dto.CartItemRequest;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final PartService partService;
    private final CartItemService cartItemService;

    public CartService(CartRepository cartRepository, PartService partService, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.partService = partService;
        this.cartItemService = cartItemService;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.getCartByUser(user);
    }

    public void createCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .build();

        cartRepository.save(cart);
    }

    public void addToCart(CartItemRequest cartItemRequest, User user) {
        Part part = partService.getPartById(String.valueOf(cartItemRequest.getPartId()));
        Cart cart = getCartByUser(user);

        if (cart.getItems().stream().anyMatch(cartItem -> cartItem.getPart().getId().equals(part.getId()))) {
            cartItemService.updateQuantity(cartItemRequest, cart, part);
        }
        else {
            cartItemService.createCartItem(part, cartItemRequest.getQuantity(), cart);
        }
        updateCart(cart);
    }

    public void updateCart(Cart cart) {
        cartRepository.save(cart);
    }
}
