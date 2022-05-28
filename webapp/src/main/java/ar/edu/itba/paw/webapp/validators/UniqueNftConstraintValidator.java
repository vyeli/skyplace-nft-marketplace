package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.service.NftService;
import ar.edu.itba.paw.webapp.form.CreateNftForm;
import ar.edu.itba.paw.webapp.validators.interfaces.UniqueNftConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueNftConstraintValidator implements ConstraintValidator<UniqueNftConstraint, CreateNftForm> {
    @Autowired
    private NftService nftService;

    @Override
    public void initialize(UniqueNftConstraint uniqueNftConstraint) {
        //
    }

    @Override
    public boolean isValid(CreateNftForm createNftForm, ConstraintValidatorContext constraintValidatorContext) {
        return !nftService.isNftCreated(createNftForm.getNftId(), createNftForm.getContractAddr(), createNftForm.getChain());
    }
}
