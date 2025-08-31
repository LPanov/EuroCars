package app.eurocars.cart.model;

import app.eurocars.cartItem.model.CartItem;
import app.eurocars.user.model.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Cart() {
        this.items = new ArrayList<>();
    }
}
