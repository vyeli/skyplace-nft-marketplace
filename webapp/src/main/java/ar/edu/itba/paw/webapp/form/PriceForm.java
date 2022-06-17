package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PriceForm {
    @Digits(integer=18, fraction=18)
    @DecimalMin(value="0.000000000000000001")
    @NotNull
    private BigDecimal price;

    public BigDecimal getPrice() {
        return price == null ? BigDecimal.ZERO : price.stripTrailingZeros();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
