package app.eurocars.cart.service;

import app.eurocars.cart.client.CartClient;
import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.client.dto.CartItemRequest;
import app.eurocars.cart.client.dto.CartItemResponse;
import app.eurocars.cart.client.mapper.DtoMapper;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CartService {

    private final CartClient cartClient;
    private final PartService partService;

    public CartService(CartClient cartClient, PartService partService) {
        this.cartClient = cartClient;
        this.partService = partService;
    }

    public void createCart(UUID userId) {
        try {
            ResponseEntity<Void> response = cartClient.createCart(userId);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to cart-svc failed] Can't save user cart for user with id = [%s]".formatted(userId));
            }
        } catch (Exception e) {
            log.error("Unable to call cart-svc");
        }
    }

    public List<CartItem> getCartItemsByUserId(UUID id) {
        ResponseEntity<List<CartItemResponse>> response = cartClient.getCartItemsByUserId(id);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Unable to get cart items for user with id = [" + id + "]");
        }

        if (response.getBody() == null) {
            throw new RuntimeException("Cart items for user with id = [" + id + "] are null");
        }

        List<CartItem> cartItems = new ArrayList<>();

        for (CartItemResponse itemResponse : response.getBody()) {
            Part partById = partService.getPartById(String.valueOf(itemResponse.getPartId()));
            CartItem cartItem = DtoMapper.getCartItems(itemResponse, partById);

            cartItems.add(cartItem);
        }

        return cartItems;
    }

    public static double getTotalWeight(CartItem cartItem) {
        return cartItem.getPart().getWeight() * cartItem.getQuantity();
    }

    public static BigDecimal getTotalPriceWithVat(List<CartItem> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            totalPrice = totalPrice.add(cartItem.getPart().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        return totalPrice.multiply(BigDecimal.valueOf(1.2));
    }

    public void addToCart(CartItemRequest cartItemRequest) {
        try {
            ResponseEntity<Void> response = cartClient.addToCart(cartItemRequest);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to cart-svc failed] Can't add item to cart for user with id = [%s]".formatted(cartItemRequest.getUserId()));
            }
        } catch (Exception e) {
            log.error("Unable to call cart-svc");
        }

    }

    public void removeFromCart(UUID itemId) {
        try {
            ResponseEntity<Void> response = cartClient.deleteCartItem(itemId);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("[Feign call to cart-svc failed] Can't remove item from cart for user with id = [%s]".formatted(itemId));
            }
        } catch (Exception e) {
            log.error("Unable to call cart-svc");
        }
    }
}
