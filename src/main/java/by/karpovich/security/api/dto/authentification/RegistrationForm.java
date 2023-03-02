package by.karpovich.security.api.dto.authentification;

import by.karpovich.security.api.dto.validation.UsernameValidation.ValidUsername;
import by.karpovich.security.api.dto.validation.emailValidator.ValidEmail;
import by.karpovich.security.jpa.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrationForm {

    @ValidUsername
    @NotBlank(message = "Enter name")
    private String username;

    @ValidEmail
    @NotBlank(message = "Enter email")
    private String email;

    private Status status;

    @NotBlank
    @NotBlank(message = "Enter password")
    private String password;
}