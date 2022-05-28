package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.webapp.validators.interfaces.ValidCategoryConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCategoryConstraintValidator implements ConstraintValidator<ValidCategoryConstraint, String> {
    @Override
    public void initialize(ValidCategoryConstraint validCategoryConstraint) {
        //
    }

    @Override
    public boolean isValid(String category, ConstraintValidatorContext constraintValidatorContext) {
        return Category.getCategories().contains(category);
    }
}
