package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "sellorders")
public class SellOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sellorders_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "sellorders_id_seq", name = "sellorders_id_seq")
    @Column(name = "id")
    private int id;

    @Column(name = "price", nullable = false, precision = 18)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "offeredFor")
    private List<BuyOrder> offers;

    @OneToOne
    @JoinColumn(name = "id_nft", referencedColumnName = "id", nullable = false)
    private Nft nft;

    /* default */ SellOrder() {
        // just for hibernate
    }
    public SellOrder(BigDecimal price, Nft nft, Category category) {
        this.price = price;
        this.nft = nft;
        this.category = category;
    }

    public List<BuyOrder> getBuyOrdersByPage(int page, int pageSize) {
        if (page <= 0) return Collections.emptyList();
        int toIndex = page * pageSize;
        if (toIndex > offers.size()) toIndex = offers.size();
        return offers.subList((page - 1) * pageSize, toIndex);
    }

    public int getBuyOrdersAmount() {
        return offers.size();
    }

    public int getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return new BigDecimal(price.stripTrailingZeros().toPlainString());
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

    public List<BuyOrder> getOffers() {
        return offers;
    }

    public Nft getNft() {
        return nft;
    }

    public void deleteBuyOrder(User buyer) {
        offers.removeIf(order -> order.getOfferedBy().getId() == buyer.getId());
    }

}
