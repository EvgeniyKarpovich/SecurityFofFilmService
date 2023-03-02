package by.karpovich.security.api.dto.authentification;

import by.karpovich.security.jpa.model.Status;
import lombok.Data;

@Data
public class RegistrationForm {

    private String username;

    private String email;

    private Status status;

    private String password;
}