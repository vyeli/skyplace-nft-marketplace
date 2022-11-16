package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.PurchaseService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.PurchaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("purchases")
@Component
public class PurchasesController {

    private final UserService userService;
    private final PurchaseService purchaseService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public PurchasesController(UserService userService, PurchaseService purchaseService) {
        this.userService = userService;
        this.purchaseService = purchaseService;
    }



    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public Response getHistoryTransactions( @QueryParam("page") @DefaultValue("1") int page){

        //TODO VALIDATE WITH JWT
        Optional<User> currentUser = userService.getCurrentUser();

        long amountPurchasesPages;
        amountPurchasesPages = purchaseService.getAmountPagesByUserId(currentUser.get().getId());

        if(page > amountPurchasesPages || page < 0 ) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<PurchaseDto> historyPurchases;
        historyPurchases = purchaseService.getAllTransactions(currentUser.get().getId(), page).stream().map(n -> PurchaseDto.fromPurchase(uriInfo, n)).collect(Collectors.toList());

        if(historyPurchases.isEmpty()){
            return Response.noContent().build();
        }
        Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<PurchaseDto>>(historyPurchases) {});

        //TODO REUSE THIS CODE
        if (page > 1)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        if (page < amountPurchasesPages)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");
        return responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", amountPurchasesPages).build(), "last")
                .build();


    }


}
