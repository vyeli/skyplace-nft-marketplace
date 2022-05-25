package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.service.ChainService;
import ar.edu.itba.paw.webapp.validators.interfaces.ValidChainConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidChainConstraintValidator implements ConstraintValidator<ValidChainConstraint, String> {
    @Autowired
    private ChainService chainService;

    @Override
    public void initialize(ValidChainConstraint validChainConstraint) {
        //
    }

    @Override
    public boolean isValid(String chain, ConstraintValidatorContext constraintValidatorContext) {
        return chainService.getChains().contains(chain);
    }
}
