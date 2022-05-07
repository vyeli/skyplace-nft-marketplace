package ar.edu.itba.paw.webapp.validators;

import ar.edu.itba.paw.webapp.validators.interfaces.ImageConstraint;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageConstraintValidator implements ConstraintValidator<ImageConstraint, MultipartFile> {

    private int maxSize;

    @Override
    public void initialize(ImageConstraint imageConstraint) {
        this.maxSize = imageConstraint.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if(multipartFile.isEmpty() || !multipartFile.getContentType().startsWith("image/")) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("File must be an image")
                    .addConstraintViolation();
            return false;
        }
        if(multipartFile.getSize() > maxSize){
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Image size must not exceed " + maxSize/1048576 + "MB")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
