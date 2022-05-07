package ar.edu.itba.paw.model;

public class Nft {
    private int id;
    private int nftId;
    private String contractAddr;
    private String name;
    private String chain;
    private int idImage;
    private int idOwner;
    private String collection;
    private String description;
    private String[] properties;
    private Integer sellOrder;

    public Nft(int id, int nftId, String contractAddr, String name, String chain, int idImage, int idOwner, String collection, String description, String[] properties, Integer sellOrder) {
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

    public int getId() {
        return id;
    }

    public int getNftId() {return nftId;}

    public String getContractAddr() {
        return contractAddr;
    }

    public String getNftName() {
        return name;
    }

    public String getChain() {
        return chain;
    }

    public int getIdImage() {
        return idImage;
    }

    public int getIdOwner() {
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

    public Integer getSellOrder() {
        return sellOrder;
    }
}
