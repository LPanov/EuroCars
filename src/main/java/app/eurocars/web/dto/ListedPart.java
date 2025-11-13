package app.eurocars.web.dto;

import app.eurocars.cart.client.dto.CartItemRequest;
import app.eurocars.part.model.Part;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ListedPart {
    private Part part;
    private CartItemRequest cartItem;
}
