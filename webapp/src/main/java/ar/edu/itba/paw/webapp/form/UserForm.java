package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class UserForm {

    @NotBlank
    @Email
    private String email;

    //TODO: VALIDATE WALLET ADDRESS FORMAT
    @NotBlank
    private String walletAddress;

    @NotBlank
    @Size(min = 6, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
