package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.Nft;
import ar.edu.itba.paw.model.SellOrder;

import javax.persistence.*;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

public class SellOrderDto {

    private int id;
    private BigDecimal price;
    private Category category;
    private NftDto nft;

    private URI self;
    private URI offers;

    private final static String SELLORDERS_URI_PREFIX = "sellorders";
    private final static String SELLOFFERS_URI_PREFIX = "selloffers";

    public static SellOrderDto fromSellOrder(final UriInfo uriInfo, final SellOrder sellOrder){
        final SellOrderDto dto = new SellOrderDto();

        final UriBuilder sellOrderUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath(SELLORDERS_URI_PREFIX)
                .path(String.valueOf(sellOrder.getId()));

        dto.id = sellOrder.getId();
        dto.price = sellOrder.getPrice();
        dto.category = sellOrder.getCategory();
        dto.nft = NftDto.fromNft(uriInfo, sellOrder.getNft());
        dto.self = sellOrderUriBuilder.build();
        dto.offers = sellOrderUriBuilder.path(SELLOFFERS_URI_PREFIX).build();

        return dto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public NftDto getNft() {
        return nft;
    }

    public void setNft(NftDto nft) {
        this.nft = nft;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getOffers() {
        return offers;
    }

    public void setOffers(URI offers) {
        this.offers = offers;
    }
}
