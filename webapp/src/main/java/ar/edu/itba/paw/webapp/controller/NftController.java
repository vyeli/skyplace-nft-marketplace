package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Publication;
import ar.edu.itba.paw.service.NftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("nfts")
@Component
public class NftController {

    private final NftService nftService;

    @Autowired
    public NftController(final NftService nftService) {
        this.nftService = nftService;
    }

    // GET /nfts - toma por defecto el path de @Path
    @GET
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response listNfts(@QueryParam("page") @DefaultValue("1") final int page) {
        // TODO: complete all parameters
        List<Publication> nftList = nftService.getAllPublications(page);

        if (nftList.isEmpty())
            return Response.noContent().build();

        // TODO: populate urls for given resources
        return Response.ok(new GenericEntity<List<Publication>>(nftList) {})
                .link("", "prev")
                .link("", "next")
                .link("", "first")
                .link("", "last")
                .build();
    }
}
