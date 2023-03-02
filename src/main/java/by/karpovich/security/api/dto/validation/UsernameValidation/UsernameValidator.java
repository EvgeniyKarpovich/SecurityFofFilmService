package by.karpovich.security.api.dto.validation.UsernameValidation;

import by.karpovich.security.jpa.model.User;
import by.karpovich.security.jpa.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null) {
            return false;
        }
        Optional<User> model = userRepository.findByUsername(username);
        return !model.isPresent();
    }
}