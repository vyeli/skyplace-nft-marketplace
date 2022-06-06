package ar.edu.itba.paw.model;


import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "nfts")
public class Nft {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nfts_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "nfts_id_seq", name = "nfts_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "nft_id", nullable = false)
    private int nftId;

    @Column(name = "contract_addr", nullable = false, length = -1)
    private String contractAddr;

    @Column(name = "nft_name", nullable = false, length = -1)
    private String nftName;

    @Enumerated(EnumType.STRING)
    private Chain chain;

    @Column(name = "id_image", nullable = false)
    private int idImage;

    @Column(name = "collection", nullable = true)
    private String collection;

    @Column(name = "description", nullable = true)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_owner", referencedColumnName = "id", nullable = false)
    private User owner;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "nftsByIdNft")
    private List<Purchase> purchasesById;

    @OneToMany(mappedBy = "nft")
    private List<Favorited> favoritedsById;

    @OneToOne(mappedBy = "nft")
    private SellOrder sellorder;

    /* default */ Nft() {
        // just for hibernate
    }

    public Nft(int nftId, String contractAddr, String nftName, Chain chain, int idImage, String collection, String description, User owner) {
        this.nftId = nftId;
        this.contractAddr = contractAddr;
        this.nftName = nftName;
        this.chain = chain;
        this.idImage = idImage;
        this.collection = collection;
        this.description = description;
        this.owner = owner;
    }

    public int getNftId() {
        return nftId;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public String getNftName() {
        return nftName;
    }

    public Chain getChain() {
        return chain;
    }

    public int getIdImage() {
        return idImage;
    }

    public String getCollection() {
        return collection;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setOwner(User newOwner) {
        this.owner = newOwner;
    }

    public Collection<Purchase> getPurchasesById() {
        return purchasesById;
    }

    public Collection<Favorited> getFavoritedsById() {
        return favoritedsById;
    }

    public SellOrder getSellOrder() {
        return sellorder;
    }

    public void setSellorder(SellOrder sellorder) {
        this.sellorder = sellorder;
    }
}
