package by.karpovich.security.api.dto.authentification;

import lombok.Data;

@Data
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
