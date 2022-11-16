package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ResponseErrorDto;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException e) {
        final List<ResponseErrorDto> errors = e.getConstraintViolations().stream()
                .map(ResponseErrorDto::fromValidationException).collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new GenericEntity<List<ResponseErrorDto>>(errors){})
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
