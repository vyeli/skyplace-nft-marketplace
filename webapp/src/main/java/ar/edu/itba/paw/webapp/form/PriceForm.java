package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class PriceForm {
    @Digits(integer=18, fraction=18)
    @DecimalMin(value="0")
    private BigDecimal price = new BigDecimal(0);

    public BigDecimal getPrice() {
        return price.stripTrailingZeros();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
