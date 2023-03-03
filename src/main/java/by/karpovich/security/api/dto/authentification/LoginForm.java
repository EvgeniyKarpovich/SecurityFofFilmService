package by.karpovich.security.api.dto.authentification;

import by.karpovich.security.api.dto.validation.usernameValidation.ValidUsernameForLogin;
import lombok.Data;

@Data
public class LoginForm {

    @ValidUsernameForLogin
    private String username;

    private String password;
}