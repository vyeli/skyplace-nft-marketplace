package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.NftService;
import ar.edu.itba.paw.webapp.form.PriceForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("dummy")
@Component
public class DummyController {

    private final NftService nftService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public DummyController(NftService nftService){
        this.nftService = nftService;
    }

    @GET
    public Response dummy(){
        return Response.ok("dummy gotten").build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response dummyResponse(@Valid PriceForm priceForm){
        return Response.ok("dummy created of $" + priceForm.getPrice()).build();
    }

}
