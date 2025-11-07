package app.eurocars.cart.client.mapper;

import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.client.dto.CartItemResponse;
import app.eurocars.part.model.Part;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static CartItem getCartItems(CartItemResponse response, Part part) {
        return CartItem.builder()
                .id(response.getId())
                .part(part)
                .quantity(response.getQuantity())
                .build();
    }
}
