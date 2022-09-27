package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.NftService;
import ar.edu.itba.paw.service.SellOrderService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.NftDto;
import ar.edu.itba.paw.webapp.dto.SellOrderDto;
import ar.edu.itba.paw.webapp.form.CreateSellOrderForm;
import ar.edu.itba.paw.webapp.form.SellNftForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("sellorders")
@Component
public class SellOrderController {

    private final SellOrderService sellOrderService;
    private final NftService nftService;
    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public SellOrderController(SellOrderService sellOrderService, UserService userService, NftService nftService) {
        this.sellOrderService = sellOrderService;
        this.userService = userService;
        this.nftService = nftService;
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response listSellOrders(
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("minPrice") @DefaultValue("0") final BigDecimal minPrice,
            @QueryParam("maxPrice") @DefaultValue("-1") final BigDecimal maxPrice,
            @QueryParam("category") final String category,
            @QueryParam("chain") final String chain,
            @QueryParam("sort") final String sort,
            @QueryParam("search") final String search,
            @QueryParam("searchFor") final String searchFor,
            @QueryParam("seller") final Integer sellerId
    ) {
        Stream<Nft> stream = nftService.getAll(page, "onSale", category, chain, minPrice, maxPrice, sort, search, searchFor)
                .stream();

        // TODO: Get only user nfts instead of all and then filter
        // Current problem: getAllPublicationsByUser used does not support all filtering query params
        if(sellerId != null) {
            stream = stream.filter(n -> n.getOwner().getId() == sellerId);
        }
        List<SellOrderDto> sellOrderList = stream.map(Nft::getSellOrder).map(s -> SellOrderDto.fromSellOrder(uriInfo, s)).collect(Collectors.toList());

        if(sellOrderList.isEmpty())
            return Response.noContent().build();

        Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<SellOrderDto>>(sellOrderList) {});
        if (page > 1)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        int lastPage = (int) Math.ceil(nftService.getAmountPublications("onSale", category, chain, minPrice, maxPrice, sort, search, "") / (double) nftService.getPageSize());
        if (page < lastPage)
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");

        return responseBuilder
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build(), "last")
                .build();
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED, })
    public Response createSellOrder(@Valid CreateSellOrderForm form) {
        // TODO: Return other response in case of exception thrown
        final SellOrder newSellOrder = sellOrderService.create(form.getPrice(), form.getNftId(), form.getCategory());
        final URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(newSellOrder.getId())).build();
        return Response.created(location).build();
    }

    @GET
    @Path("/{id}")
    @Produces({ MediaType.APPLICATION_JSON, })
    public Response getSellOrder(@PathParam("id") int id) {
        Optional<SellOrder> maybeSellOrder = sellOrderService.getOrderById(id);
        if(maybeSellOrder.isPresent()){
            SellOrderDto dto = SellOrderDto.fromSellOrder(uriInfo, maybeSellOrder.get());
            return Response.ok(dto).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED, })
    public Response updateSellOrder(@PathParam("id") int id, @Valid SellNftForm form){
        boolean result = sellOrderService.update(id, form.getCategory(), form.getPrice());
        if(result)
            return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSellOrder(@PathParam("id") int id) {
        sellOrderService.delete(id);
        return Response.noContent().build();
    }
}
