package by.karpovich.security.api.controller;

import by.karpovich.security.api.dto.authentification.JwtResponse;
import by.karpovich.security.api.dto.authentification.LoginForm;
import by.karpovich.security.api.dto.authentification.RegistrationForm;
import by.karpovich.security.jpa.repository.UserRepository;
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

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        return userService.signIn(loginForm);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationForm signUpRequest) {
        userService.signUp(signUpRequest);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}