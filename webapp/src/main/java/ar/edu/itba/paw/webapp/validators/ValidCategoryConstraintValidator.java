package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.service.CategoryService;
import ar.edu.itba.paw.webapp.validators.interfaces.ValidCategoryConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidCategoryConstraintValidator implements ConstraintValidator<ValidCategoryConstraint, String> {
    @Autowired
    private CategoryService categoryService;

    @Override
    public void initialize(ValidCategoryConstraint validCategoryConstraint) {
        //
    }

    @Override
    public boolean isValid(String category, ConstraintValidatorContext constraintValidatorContext) {
        return categoryService.getCategories().contains(category);
    }
}
