package app.eurocars.web.dto;

import app.eurocars.category.model.Category;
import app.eurocars.engine.model.Engine;
import app.eurocars.manufacturer.model.Manufacturer;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddPartRequest {
    private UUID id;

    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Description cannot be empty")
    private String description;
    private String imgUrls;
    private String additionalInformation;
    private String otherNumbers;

    @NotNull(message = "Price cannot be empty")
    private BigDecimal price;
    private double weight;
    private Long manufacturer;
    private Long category;
    private Long engine;
}
