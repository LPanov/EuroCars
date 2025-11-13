package app.eurocars.cart.client;

import app.eurocars.cart.client.dto.CartItemRequest;
import app.eurocars.cart.client.dto.CartItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "cart-svc", url = "http://localhost:8081/api/v1")
public interface CartClient {

    @GetMapping("/test")
    ResponseEntity<String> getHelloMessage(@RequestParam(name = "name") String name);

    @PostMapping("/cart")
    ResponseEntity<Void> createCart(@RequestParam(name = "userId") UUID userId);

    @GetMapping("/cart")
    ResponseEntity<List<CartItemResponse>> getCartItemsByUserId(@RequestParam("userId") UUID userId);

    @PostMapping("/cart/item")
    ResponseEntity<Void> addToCart(@RequestBody CartItemRequest cartItemRequest);

    @DeleteMapping("/cart/item")
    ResponseEntity<Void> deleteCartItem(@RequestParam("itemId") UUID itemId);
}
