package by.karpovich.security.api.dto.validation.emailValidator;

import by.karpovich.security.jpa.model.User;
import by.karpovich.security.jpa.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null) {
            return false;
        }
        Optional<User> model = userRepository.findByEmail(username);
        return !model.isPresent();
    }
}
