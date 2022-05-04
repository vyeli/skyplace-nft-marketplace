package ar.edu.itba.paw.model;

public class User {

    private long id;
    private String email;
    private String username;
    private String wallet;
    private String walletChain;
    private String password;
    private String role;

    public User(long id, String email, String username, String wallet, String chain, String password, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.wallet = wallet;
        this.walletChain = chain;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getWallet() {
        return wallet;
    }

    public String getWalletChain() { return walletChain; }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
