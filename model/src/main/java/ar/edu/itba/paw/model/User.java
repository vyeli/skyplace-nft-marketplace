package ar.edu.itba.paw.model;

public class User {

    private long id;
    private String email;
    private String username;
    private String wallet;
    private String password;

    public User(long id, String email, String username, String wallet, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.wallet = wallet;
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

    public String getPassword() {
        return password;
    }
}