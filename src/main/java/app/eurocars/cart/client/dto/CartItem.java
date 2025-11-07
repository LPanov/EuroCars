package app.eurocars.cart.client.dto;

import app.eurocars.part.model.Part;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class CartItem {
    private UUID id;
    private Part part;
    private int quantity;
}

