package ar.edu.itba.paw.webapp.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.validators.interfaces.PasswordsEqualConstraint;

public class PasswordsEqualConstraintValidator implements
        ConstraintValidator<PasswordsEqualConstraint, UserForm> {

    @Override
    public void initialize(PasswordsEqualConstraint arg0) {
    }

    @Override
    public boolean isValid(UserForm user, ConstraintValidatorContext arg1) {
        return user.getPassword().equals(user.getPasswordRepeat());
    }
}
