package app.eurocars.web.dto;

import app.eurocars.user.model.Country;
import app.eurocars.user.model.Role;
import app.eurocars.util.annotations.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserRequest {
    private UUID id;

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

    private String zipCode;

    private String city;

    private String state;

    private Country country;

    private Role role;

    @Password(minLength = 5,
            containsUpperCase = true,
            containsLowerCase = true,
            containsDigit = true)
    private String password;


}
