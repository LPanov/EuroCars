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

    public BigDecimal getTotalPriceWithVat() {
        return this.part.getPrice().multiply(new BigDecimal(this.quantity)).multiply(new BigDecimal("1.2"));
    }

    public double getTotalWeight() {
        return this.part.getWeight() * this.quantity;
    }
}



