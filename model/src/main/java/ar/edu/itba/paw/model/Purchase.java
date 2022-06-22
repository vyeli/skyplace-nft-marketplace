package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "purchases_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "purchases_id_seq", name = "purchases_id_seq")
    @Column(name = "id")
    private Integer id;

    @Column(name = "price", nullable = false, precision = 18)
    private BigDecimal price;

    @Column(name = "buy_date", nullable = false)
    private Date buyDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_nft", referencedColumnName = "id", nullable = false)
    private Nft nftsByIdNft;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_buyer", referencedColumnName = "id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_seller", referencedColumnName = "id", nullable = false)
    private User seller;

    @Enumerated(EnumType.STRING)
    private StatusPurchase status;

    @Column(name = "tx")
    private String txHash;

    /* default */ Purchase() {
        // just for hibernate
    }

    public Purchase(BigDecimal price, Date buyDate, Nft nftsByIdNft, User buyer, User seller, StatusPurchase status, String txHash) {
        this.price = price;
        this.buyDate = buyDate;
        this.nftsByIdNft = nftsByIdNft;
        this.buyer = buyer;
        this.seller = seller;
        this.status = status;
        this.txHash = txHash;
    }

    public Purchase(int id, BigDecimal price, Date buyDate, Nft nftsByIdNft, User buyer, User seller, StatusPurchase status, String txHash) {
        this.id = id;
        this.price = price;
        this.buyDate = buyDate;
        this.nftsByIdNft = nftsByIdNft;
        this.buyer = buyer;
        this.seller = seller;
        this.status = status;
        this.txHash = txHash;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price.stripTrailingZeros();
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public Nft getNftsByIdNft() {
        return nftsByIdNft;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public StatusPurchase getStatus() {return status;}

    public String getTxHash() {
        return txHash;
    }
}
