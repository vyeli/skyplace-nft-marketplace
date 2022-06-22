package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.service.NftService;
import ar.edu.itba.paw.webapp.validators.interfaces.UniqueNftConstraint;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueNftConstraintValidator implements ConstraintValidator<UniqueNftConstraint, Object> {
    @Autowired
    private NftService nftService;

    private String nftId;
    private String contractAddr;
    private String chain;

    @Override
    public void initialize(UniqueNftConstraint arg0) {
        this.nftId = arg0.nftId();
        this.contractAddr = arg0.contractAddr();
        this.chain = arg0.chain();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(obj);
        int nftIdObj = (int)wrapper.getPropertyValue(nftId);
        String contractAddrObj = (String)wrapper.getPropertyValue(contractAddr);
        String chainObj = (String)wrapper.getPropertyValue(chain);
        return !nftService.isNftCreated(nftIdObj, contractAddrObj, chainObj);
    }
}
