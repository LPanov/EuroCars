package app.eurocars.cart.repository;

import app.eurocars.cart.model.Cart;
import app.eurocars.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart getCartByUser(User user);
}
