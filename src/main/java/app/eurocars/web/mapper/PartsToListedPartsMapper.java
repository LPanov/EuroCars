package app.eurocars.web.mapper;

import app.eurocars.cart.client.dto.CartItemRequest;
import app.eurocars.part.model.Part;
import app.eurocars.web.dto.ListedPart;

import java.util.ArrayList;
import java.util.List;

public class PartsToListedPartsMapper {

    public static List<ListedPart> map(List<Part> parts){
        List<ListedPart> listedParts = new ArrayList<>();

        for (Part part : parts) {
            ListedPart listedPart = ListedPart.builder()
                    .part(part)
                    .cartItem(CartItemRequest.builder()
                            .partId(part.getId())
                            .build())
                    .build();

            listedParts.add(listedPart);
        }

        return listedParts;
    }
}
