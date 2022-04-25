package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.interfaces.ImageConstraint;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

public class SellNftForm extends UpdateSellOrderForm {

    @NotBlank
    private String name;

    @Digits(message = "Id is not a valid number!", integer = 8, fraction = 0)
    @Min(value=0L, message = "ID must be a positive number")
    private int nftId;

    @NotBlank
    private String nftContract;

    @NotBlank
    private String chain;

    @ImageConstraint(maxSize = 2097152)     // 2MB
    private MultipartFile image;

    @NotEmpty
    @Email
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNftId() {
        return nftId;
    }

    public void setNftId(int nftId) {
        this.nftId = nftId;
    }

    public String getNftContract() {
        return nftContract;
    }

    public void setNftContract(String nftContract) {
        this.nftContract = nftContract;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublish() {
        return "";
    }
}
