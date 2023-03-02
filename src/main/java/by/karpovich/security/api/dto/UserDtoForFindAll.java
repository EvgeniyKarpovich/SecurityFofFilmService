package by.karpovich.security.api.dto;

import by.karpovich.security.jpa.model.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserDtoForFindAll {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String username;

    private String email;

    private List<String> roles;

    private Status status;
}
