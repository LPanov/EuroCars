package app.eurocars.web.dto;

import app.eurocars.util.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {
    @NotNull
    private String ownerName;
    @Email
    private String email;
    @NotNull
    private String companyAddress;
    @Password
    private String password;

    private Boolean pricesWithVAT;
    private Boolean wholesalePrices;
    private Boolean productsOrder;
    private Boolean showWeight;
}
