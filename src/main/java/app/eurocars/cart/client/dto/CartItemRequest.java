package app.eurocars.cart.client.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CartItemRequest {
    private UUID userId;

    @NotNull
    @Min(1)
    private int quantity;

    @NotNull
    private UUID partId;
}
