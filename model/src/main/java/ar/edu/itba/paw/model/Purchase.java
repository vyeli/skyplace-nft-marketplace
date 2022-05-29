package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
    private Timestamp buyDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_nft", referencedColumnName = "id", nullable = false)
    private Nft nftsByIdNft;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_buyer", referencedColumnName = "id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_seller", referencedColumnName = "id", nullable = false)
    private User seller;

    /* default */ Purchase() {
        // just for hibernate
    }

    public Purchase(BigDecimal price, Timestamp buyDate, Nft nftsByIdNft, User buyer, User seller) {
        this.price = price;
        this.buyDate = buyDate;
        this.nftsByIdNft = nftsByIdNft;
        this.buyer = buyer;
        this.seller = seller;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return new BigDecimal(price.stripTrailingZeros().toPlainString());
    }

    public Timestamp getBuyDate() {
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
}
