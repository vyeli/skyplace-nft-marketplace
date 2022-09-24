package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@Path("users")
@Component
public class UserController {

    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /users
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED, })
    @POST
    public Response createUser(@Valid final UserForm userForm) {
        final User newUser = userService.create(userForm.getEmail(), userForm.getUsername(), userForm.getWalletAddress(), userForm.getWalletChain(), userForm.getPassword());
        // appends the new ID to the path of this route (/users)
        final URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newUser.getId())).build();
        return Response.created(location).build();
    }

    // GET /users/{id}
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") int id) {
        Optional<UserDto> maybeUser = userService.getUserById(id).map(user -> UserDto.fromUser(uriInfo, user));
        if (maybeUser.isPresent()) {
            return Response.ok(maybeUser.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
