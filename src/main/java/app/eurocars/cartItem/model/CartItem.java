package app.eurocars.cartItem.model;

import app.eurocars.cart.model.Cart;
import app.eurocars.part.model.Part;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    public BigDecimal getTotalPriceWithVat() {
        return part.getPrice().multiply(new BigDecimal(quantity)).multiply(new BigDecimal("1.2"));
    }

    public double getTotalWeight() {
        return part.getWeight() * quantity;
    }
}
