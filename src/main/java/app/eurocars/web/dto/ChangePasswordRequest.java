package app.eurocars.web.dto;

import app.eurocars.util.annotations.Password;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotNull(message = "Email cannot be empty")
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @Password(minLength = 5,
            containsUpperCase = true,
            containsLowerCase = true,
            containsDigit = true,
            message = "Password does not match the requirements.\nPassword must contains: Upper/Lower symbol, digit and min length of 5.")
    private String confirmPassword;
}
