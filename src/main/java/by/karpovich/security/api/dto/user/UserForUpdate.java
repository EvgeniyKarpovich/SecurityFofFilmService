package by.karpovich.security.api.dto.user;

import by.karpovich.security.api.dto.validation.usernameValidation.ValidUsername;
import by.karpovich.security.api.dto.validation.emailValidator.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForUpdate {

    @ValidUsername
    @NotBlank(message = "Enter name")
    private String username;

    @ValidEmail
    @NotBlank(message = "Enter email")
    private String email;
}
