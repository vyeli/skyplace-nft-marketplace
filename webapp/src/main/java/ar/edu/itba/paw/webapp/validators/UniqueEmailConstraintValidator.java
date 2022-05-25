package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.validators.interfaces.UniqueEmailConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailConstraintValidator implements ConstraintValidator<UniqueEmailConstraint, String> {
    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueEmailConstraint uniqueEmailConstraint) {
        //
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.getUserByEmail(email).isPresent();
    }
}
