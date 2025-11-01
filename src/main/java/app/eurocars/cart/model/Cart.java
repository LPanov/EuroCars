package app.eurocars.cart.model;

import app.eurocars.cartItem.model.CartItem;
import app.eurocars.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Builder
@Getter
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
    private List<CartItem> items;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public BigDecimal getWholePrice() {
        BigDecimal price = BigDecimal.ZERO;
        for (CartItem item : items) {
            price = price.add(item.getTotalPriceWithVat());
        }

        return price;
    }
}
