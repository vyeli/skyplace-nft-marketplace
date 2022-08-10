package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

public class ValidationErrorDto {

    private String message;
    private String path;

    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> vex){
        final ValidationErrorDto dto = new ValidationErrorDto();
        dto.message = vex.getMessage();
        dto.path = vex.getPropertyPath().toString();
        return dto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
