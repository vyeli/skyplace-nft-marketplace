package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.validators.interfaces.ImageConstraint;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageConstraintValidator implements ConstraintValidator<ImageConstraint, MultipartFile> {
    private static final long MAX_SIZE = 1000000; // 1MB

    @Override
    public void initialize(ImageConstraint imageConstraint) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        return !multipartFile.isEmpty() && multipartFile.getContentType().startsWith("image/") && multipartFile.getSize() < MAX_SIZE;
    }
}
