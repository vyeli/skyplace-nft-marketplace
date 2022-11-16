package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ResponseErrorDto;
import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Override
    public Response toResponse(AccessDeniedException e) {
        final ResponseErrorDto error = ResponseErrorDto.fromAccessDeniedException(e);

        return Response.status(Response.Status.FORBIDDEN)
                .entity(new GenericEntity<ResponseErrorDto>(error){})
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
