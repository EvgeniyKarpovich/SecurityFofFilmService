package by.karpovich.security.api.dto.validation.usernameValidation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UsernameValidatorForLogin.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsernameForLogin {

    String message() default "Wrong login or Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
