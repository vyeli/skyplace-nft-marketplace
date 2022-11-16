package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Nft;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class NftDto {

    private int nftId;
    private String contractAddr;
    private String nftName;
    private int idImage;
    private String collection;
    private String description;
    private boolean isDeleted;
    private String chain;

    // hyperlinks to itself and to other entities
    private URI self;
    private URI owner;
    private URI sellorder;
    private URI purchases;

    // returns the DTO representation of the Nft
    public static NftDto fromNft(final UriInfo uriInfo, final Nft nft) {
        final NftDto dto = new NftDto();

        final UriBuilder nftUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("nfts")
                .path(String.valueOf(nft.getId()));
        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("users")
                .path(String.valueOf(nft.getOwner().getId()));
        final UriBuilder purchasesUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("purchases");

        if (nft.getSellOrder() != null) {
            final UriBuilder sellorderUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("sellorders")
                    .path(String.valueOf(nft.getSellOrder().getId()));
            // /orders/12
            dto.sellorder = sellorderUriBuilder.build();
        } else {
            dto.sellorder = URI.create("");
        }

        dto.self = nftUriBuilder.build();
        dto.owner = userUriBuilder.build();
        // /purchases?nftId=21
        dto.purchases = purchasesUriBuilder.queryParam("nftId", String.valueOf(nft.getId())).build();

        dto.collection = nft.getCollection();
        dto.contractAddr = nft.getContractAddr();
        dto.description = nft.getDescription();
        dto.idImage = nft.getIdImage();
        dto.isDeleted = nft.isDeleted();
        dto.chain = nft.getChain().name();
        dto.nftId = nft.getNftId();
        dto.nftName = nft.getNftName();
        return dto;
    }

    public int getNftId() {
        return nftId;
    }

    public void setNftId(int nftId) {
        this.nftId = nftId;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public String getNftName() {
        return nftName;
    }

    public void setNftName(String nftName) {
        this.nftName = nftName;
    }

    public int getIdImage() {
        return idImage;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getPurchases() {
        return purchases;
    }

    public void setPurchases(URI purchases) {
        this.purchases = purchases;
    }

    public URI getSellorder() {
        return sellorder;
    }

    public void setSellorder(URI sellorder) {
        this.sellorder = sellorder;
    }

    public URI getOwner() {
        return owner;
    }

    public void setOwner(URI owner) {
        this.owner = owner;
    }
}
