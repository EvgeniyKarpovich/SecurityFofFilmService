package by.karpovich.security.api.dto.authentification;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {

    private Long id;
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String email;
    private List<String> roles;
}
