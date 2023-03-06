package by.karpovich.security.api.controller;

import by.karpovich.security.api.dto.authentification.*;
import by.karpovich.security.exception.TokenRefreshException;
import by.karpovich.security.jpa.model.RefreshToken;
import by.karpovich.security.jpa.repository.UserRepository;
import by.karpovich.security.security.JwtUtils;
import by.karpovich.security.service.RefreshTokenService;
import by.karpovich.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        return userService.signIn(loginForm);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationForm signUpRequest) {
        userService.signUp(signUpRequest);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user.getUsername());

                    TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse();
                    tokenRefreshResponse.setAccessToken(token);
                    tokenRefreshResponse.setRefreshToken(requestRefreshToken);
                    return ResponseEntity.ok(tokenRefreshResponse);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}