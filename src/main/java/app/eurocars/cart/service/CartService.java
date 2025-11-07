package app.eurocars.cart.service;

import app.eurocars.cart.client.CartClient;
import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.client.dto.CartItemResponse;
import app.eurocars.cart.client.mapper.DtoMapper;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import jakarta.mail.FetchProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<CartItemResponse> responses = cartClient.getCartItemsByUserId(id);
        List<CartItem> cartItems = new ArrayList<>();

        for (CartItemResponse response : responses) {
            Part partById = partService.getPartById(String.valueOf(response.getPartId()));
            CartItem cartItem = DtoMapper.getCartItems(response, partById);

            cartItems.add(cartItem);
        }

        return cartItems;
    }

    public static double getTotalWeight(List<CartItem> cartItems) {
        return cartItems.stream().mapToDouble(CartItem::getQuantity).sum();
    }

    public static BigDecimal getTotalPriceWithVat(List<CartItem> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            totalPrice = totalPrice.add(cartItem.getPart().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        return totalPrice.multiply(BigDecimal.valueOf(1.2));
    }
}
