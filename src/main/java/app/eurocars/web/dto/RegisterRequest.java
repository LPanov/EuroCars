package app.eurocars.web.dto;

import app.eurocars.util.annotations.Password;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @Length(min = 5, message = "Company name must be at least 5 symbols long")
    private String companyName;

    @NotNull
    @Length(min = 5, message = "Owner name must be at least 5 symbols long")
    private String ownerName;

    @NotNull
    @NotEmpty(message = "Company address cannot be empty")
    private String companyAddress;

    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Phone number cannot be empty")
    private String phoneNumber;

    @Password(minLength = 5,
            containsUpperCase = true,
            containsLowerCase = true,
            containsDigit = true,
            message = "Password does not match the requirements.\nPassword must contains: Upper/Lower symbol, digit and min length of 5.")
    private String password;
}
