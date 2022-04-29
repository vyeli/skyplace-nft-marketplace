package ar.edu.itba.paw.model;

public class Nft {
    private long id;
    private long nft_id;
    private String contract_addr;
    private String name;
    private String chain;
    private long id_image;
    private long id_owner;
    private String collection;
    private String description;
    private String[] properties;
    private Long sell_order;

    public Nft(long id, long nft_id, String contract_addr, String name, String chain, long id_image, long id_owner, String collection, String description, String[] properties, Long sell_order) {
        this.id = id;
        this.nft_id = nft_id;
        this.contract_addr = contract_addr;
        this.name = name;
        this.chain = chain;
        this.id_image = id_image;
        this.id_owner = id_owner;
        this.collection = collection;
        this.description = description;
        this.properties = properties;
        this.sell_order = sell_order;
    }

    public long getId() {
        return id;
    }

    public long getNft_id() {return nft_id;}

    public String getContract_addr() {
        return contract_addr;
    }

    public String getNft_name() {
        return name;
    }

    public String getChain() {
        return chain;
    }

    public long getId_image() {
        return id_image;
    }

    public long getId_owner() {
        return id_owner;
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

    public Long getSell_order() {
        return sell_order;
    }
}
