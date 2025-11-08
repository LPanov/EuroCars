package app.eurocars.cart.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CartItemResponse {
    private UUID id;
    private UUID partId;
    private int quantity;
}
