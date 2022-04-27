package ar.edu.itba.paw.model;

public class NftCard {

    private long img;
    private String name;
    private String chain;
    private float price;
    private int score;
    private String seller_email;
    private String descr;
    private String contract_addr;
    private long id_nft;
    private long id_product;

    public NftCard(long img, String name, String chain, float price, int score, String seller_email, String descr, String contract_addr, long id_nft, long id_product) {
        this.img = img;
        this.name = name;
        this.chain = chain;
        this.price = price;
        this.score = score;
        this.seller_email = seller_email;
        this.descr = descr;
        this.contract_addr = contract_addr;
        this.id_nft = id_nft;
        this.id_product = id_product;
    }

    public long getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getChain() { return chain; }

    public float getPrice() {
        return price;
    }

    public int getScore() {
        return score;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public String getDescr() {
        return descr;
    }

    public String getContract_addr() {
        return contract_addr;
    }

    public long getId_nft() {
        return id_nft;
    }

    public long getId_product() {
        return id_product;
    }
}
