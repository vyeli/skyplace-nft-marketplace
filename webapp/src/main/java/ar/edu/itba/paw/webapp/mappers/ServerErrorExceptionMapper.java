package ar.edu.itba.paw.webapp.mappers;

import ar.edu.itba.paw.webapp.dto.ResponseErrorDto;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerErrorExceptionMapper implements ExceptionMapper<ServerErrorException> {

    @Override
    public Response toResponse(ServerErrorException e) {
        final ResponseErrorDto error = ResponseErrorDto.fromServerErrorException(e);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new GenericEntity<ResponseErrorDto>(error){})
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
