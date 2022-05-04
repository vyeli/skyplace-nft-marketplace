package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class SellNftForm {
    private String category;

    @Digits(integer=18, fraction=18, message = "Price is not a valid number or exceeds maximum precision!")
    @DecimalMin(value="0", message = "Price must be a positive number")
    private BigDecimal price = new BigDecimal(0);

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return new BigDecimal(price.stripTrailingZeros().toPlainString());
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPublish(){return "";}

}
