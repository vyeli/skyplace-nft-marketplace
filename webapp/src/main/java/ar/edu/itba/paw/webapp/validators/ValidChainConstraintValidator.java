package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.model.Chain;
import ar.edu.itba.paw.webapp.validators.interfaces.ValidChainConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidChainConstraintValidator implements ConstraintValidator<ValidChainConstraint, String> {

    @Override
    public void initialize(ValidChainConstraint validChainConstraint) {
        //
    }

    @Override
    public boolean isValid(String chain, ConstraintValidatorContext constraintValidatorContext) {
        return Chain.getChains().contains(chain);
    }
}
