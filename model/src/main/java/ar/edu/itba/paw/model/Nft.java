package ar.edu.itba.paw.model;

public class Nft {
    private long id;
    private long nftId;
    private String contractAddr;
    private String name;
    private String chain;
    private long idImage;
    private long idOwner;
    private String collection;
    private String description;
    private String[] properties;
    private Long sellOrder;

    public Nft(long id, long nftId, String contractAddr, String name, String chain, long idImage, long idOwner, String collection, String description, String[] properties, Long sellOrder) {
        this.id = id;
        this.nftId = nftId;
        this.contractAddr = contractAddr;
        this.name = name;
        this.chain = chain;
        this.idImage = idImage;
        this.idOwner = idOwner;
        this.collection = collection;
        this.description = description;
        this.properties = properties;
        this.sellOrder = sellOrder;
    }

    public Nft() {
        this.name = "Deleted NFT";
    }

    public long getId() {
        return id;
    }

    public long getNftId() {return nftId;}

    public String getContractAddr() {
        return contractAddr;
    }

    public String getNftName() {
        return name;
    }

    public String getChain() {
        return chain;
    }

    public long getIdImage() {
        return idImage;
    }

    public long getIdOwner() {
        return idOwner;
    }

    public String getCollection() {
        return collection;
    }

    public String getDescription() {
        return description;
    }

    public String[] getProperties() {
        return properties;
    }

    public Long getSellOrder() {
        return sellOrder;
    }
}
