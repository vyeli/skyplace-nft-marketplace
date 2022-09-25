package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.NftService;
import ar.edu.itba.paw.service.ReviewService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.NftDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("users")
@Component
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
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

    @GET()
    @Path("/{id}/reviews")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response listUserReviews(
            @PathParam("id") final int revieweeId,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("reviewer") @DefaultValue("-1") final int reviewerId
    ) {
        Stream<Review> reviewStream = reviewService.getUserReviews(page, revieweeId).stream();
        List<ReviewDto> reviewList;

        if(reviewerId > 0)
            reviewStream = reviewStream.filter(r -> r.getUsersByIdReviewer().getId() == reviewerId);
        reviewList = reviewStream.map(n -> ReviewDto.fromReview(uriInfo, n)).collect(Collectors.toList());

        if(reviewList.isEmpty())
            return Response.noContent().build();

        Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<ReviewDto>>(reviewList) {});
        if (page > 1)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        int lastPage = (int) Math.ceil(reviewService.getUserReviewsAmount(revieweeId) / (double) reviewService.getPageSize());
        if (page < lastPage)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");

        return responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build(), "last")
                .build();
    }

}
