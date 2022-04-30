package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validators.interfaces.ImageConstraint;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CreateNftForm {
    @Digits(message = "Id is not a valid number!", integer = 8, fraction = 0)
    @Min(value=0L, message = "ID must be a positive number")
    @NotNull
    private long nft_id;

    @NotBlank
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Contract must begin with 0x and contain 40 characters (a-f or 0-9)")
    private String contract_addr;

    @NotBlank
    private String name;

    @NotBlank
    private String chain;

    @ImageConstraint(maxSize = 2097152)     // 2MB
    private MultipartFile image;

    @NotBlank
    private String collection;

    private String description;

    private String[] properties;

    public long getNft_id() {
        return nft_id;
    }

    public void setId(long nft_id) {
        this.nft_id = nft_id;
    }

    public String getContract_addr() {
        return contract_addr;
    }

    public void setContract_addr(String contract_addr) {
        this.contract_addr = contract_addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public String getPublish() {
        return "";
    }
}
