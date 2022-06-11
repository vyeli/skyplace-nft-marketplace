package ar.edu.itba.paw.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name = "users_id_seq")
    @Column(name = "id")
    private Integer id;

    // TODO: check string lengths

    @Column(nullable = false, length = -1)
    private String username;

    @Column(nullable = false, length = -1)
    private String wallet;

    @Column(nullable = false, length = -1, unique = true)
    private String email;

    @Column(nullable = false, length = -1)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="wallet_chain", nullable = false, length = -1)
    private Chain walletChain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = -1)
    private Role role;

    @OneToMany(mappedBy = "offeredBy", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<BuyOrder> buyOrders;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Collection<Favorited> favorited;

    @OneToMany(mappedBy = "owner")
    private Collection<Nft> nftsOwned;

    @OneToMany(mappedBy = "buyer")
    private List<Purchase> purchaseHistory;

    @OneToMany(mappedBy = "seller")
    private List<Purchase> saleHistory;

    @OneToMany(mappedBy = "usersByIdReviewer", cascade = CascadeType.ALL)
    private Collection<Review> reviewsCreated;

    @OneToMany(mappedBy = "usersByIdReviewee", cascade = CascadeType.ALL)
    private Collection<Review> reviewsReceived;

    /* default */ User() {
        // just for hibernate
    }

    public User(String username, String wallet, String email, String password, Chain walletChain, Role role) {
        this.username = username;
        this.wallet = wallet;
        this.email = email;
        this.password = password;
        this.walletChain = walletChain;
        this.role = role;
    }

    public boolean isNftOwner(int nftId) {
        for (Nft nft : nftsOwned) {
            if (nft.getId() == nftId)
                return true;
        }
        return false;
    }

    public int getTransactionAmount() {
        return saleHistory.size() + purchaseHistory.size();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getWallet() {
        return wallet;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Chain getWalletChain() {
        return walletChain;
    }

    public Role getRole() {
        return role;
    }

}
