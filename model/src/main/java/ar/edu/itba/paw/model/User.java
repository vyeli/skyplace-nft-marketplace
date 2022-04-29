package ar.edu.itba.paw.model;

public class User {

    private long id;
    private String email;
    private String username;
    private String wallet;
    private String chain;
    private String password;

    public User(long id, String email, String username, String wallet, String chain, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.wallet = wallet;
        this.chain = chain;
        this.password = password;
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

    public String getChain() { return chain; }

    public String getPassword() {
        return password;
    }
}
