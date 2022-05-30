package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.validators.interfaces.UniqueUsernameConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameConstraintValidator implements ConstraintValidator<UniqueUsernameConstraint, String> {
    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueUsernameConstraint uniqueUsernameConstraint) {
        //
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.getUserByUsername(username).isPresent();
    }
}
