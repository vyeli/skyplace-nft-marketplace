package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.interfaces.ImageConstraint;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

public class SellNftForm extends UpdateSellOrderForm {

    @NotBlank
    private String name;

    @Digits(message = "Id is not a valid number!", integer = 8, fraction = 0)
    @Min(value=0L, message = "ID must be a positive number")
    private int id;

    @NotBlank
    private String contract;

    @NotBlank
    private String chain;

    @ImageConstraint(maxSize = 2097152)     // 2MB
    private MultipartFile image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
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

    public String getPublish() {
        return "";
    }
}
