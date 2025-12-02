package app.eurocars.web.dto;

import app.eurocars.util.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Company name cannot be empty")
    private String ownerName;
    @Email(message = "Invalid email address")
    private String email;
    @NotNull
    @NotEmpty(message = "Company address cannot be empty")
    private String companyAddress;
    @Password(minLength = 5,
            containsUpperCase = true,
            containsLowerCase = true,
            containsDigit = true,
            message = "Password does not match the requirements." +
                    "\nPassword must contains: Upper/Lower symbol, digit and min length of 5.")
    private String password;

    private Boolean pricesWithVAT;
    private Boolean wholesalePrices;
    private Boolean productsOrder;
    private Boolean showWeight;
}
