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
    @Length(min = 5)
    private String ownerName;

    @NotNull
    @NotEmpty
    private String companyAddress;

    @Email
    private String email;

    @NotNull
    private String phoneNumber;

    @Password(minLength = 5,
            containsUpperCase = true,
            containsLowerCase = true,
            containsDigit = true)
    private String password;
}
