package by.karpovich.security.dto;

import by.karpovich.security.jpa.model.Status;
import lombok.Data;

@Data
public class RegistrationForm {

    private String username;

    private String email;

    private Status status;

    private String password;
}