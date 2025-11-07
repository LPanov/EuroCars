package app.eurocars.cart.client.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItemResponse {
    private UUID id;
    private UUID partId;
    private int quantity;
}
