package ar.edu.itba.paw.webapp.dto;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;

public class ValidationErrorDto {

    private Integer status;
    private SourceDto source;
    private String title;

    public static ValidationErrorDto fromValidationException(final ConstraintViolation<?> vex) {
        final ValidationErrorDto dto = new ValidationErrorDto();
        Map<String, String> sourceMap = new HashMap<>();

        dto.status = 400;
        sourceMap.put("pointer", getPointerString(vex.getPropertyPath().toString()));
        dto.source = SourceDto.toSourceList(sourceMap);
        dto.title = vex.getMessage();
        return dto;
    }

    private static String getPointerString(String propertyPath) {
        if(propertyPath == null)
            return "";
        String[] pointerStrings = propertyPath.split("\\.");
        return pointerStrings[pointerStrings.length-1];
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public SourceDto getSource() {
        return source;
    }

    public void setSource(SourceDto source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
