package app.eurocars.cartItem.service;

import app.eurocars.cart.model.Cart;
import app.eurocars.cartItem.model.CartItem;
import app.eurocars.cartItem.repository.CartItemRepository;
import app.eurocars.part.model.Part;
import app.eurocars.web.dto.CartItemRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartItemService {


    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public void createCartItem(Part part, int quantity, Cart cart) {
        CartItem cartItem = CartItem.builder()
                .part(part)
                .quantity(quantity)
                .cart(cart)
                .build();

        cartItemRepository.save(cartItem);
    }

    public void updateQuantity(CartItemRequest cartItemRequest, Cart cart, Part part) {
        CartItem cartItem = cart.getItems().stream().filter(item -> item.getPart().getId().equals(cartItemRequest.getPartId())).findFirst().get();
        cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());

        cartItemRepository.save(cartItem);
    }

    public void deleteCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
