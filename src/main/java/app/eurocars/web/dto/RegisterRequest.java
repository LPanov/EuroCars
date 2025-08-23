package app.eurocars.web.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterRequest {
    @NotNull
    private String companyName;

    @NotNull
    private String ownerName;

    @NotNull
    @NotEmpty
    private String companyAddress;

    @Email
    private String email;

    private String phoneNumber;

    @Length(min = 6)
    private String password;
}
