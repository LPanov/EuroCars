package app.eurocars.web.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private UUID partId;
    private int quantity;
}
