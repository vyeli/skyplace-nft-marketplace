package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.validators.interfaces.ImageConstraint;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageConstraintValidator implements ConstraintValidator<ImageConstraint, MultipartFile> {

    private double maxSizeMB;

    @Override
    public void initialize(ImageConstraint imageConstraint) {
        this.maxSizeMB = imageConstraint.maxSizeMB();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFile == null || multipartFile.isEmpty() || !multipartFile.getContentType().startsWith("image/")) {
            return false;
        }
        return multipartFile.getSize() <= maxSizeMB * 1048576;
    }
}
