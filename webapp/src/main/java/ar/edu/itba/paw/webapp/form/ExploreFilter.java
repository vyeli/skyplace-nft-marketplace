package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class ExploreFilter {
    private String search;

    private String category = "all";

    private String chain = "all";

    private String sort = "";

    @DecimalMin(value="0")
    @Digits(integer=8, fraction=8)
    private BigDecimal minPrice = new BigDecimal(0);

    @DecimalMin(value="0")
    @Digits(integer=8, fraction=8)
    private BigDecimal maxPrice = new BigDecimal(0);

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCategory() {
        if(category.equals(""))
            setCategory("all");
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChain() {
        if(chain.equals(""))
            setChain("all");
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public BigDecimal getMinPrice() {
        return new BigDecimal(minPrice.stripTrailingZeros().toPlainString());
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return new BigDecimal(maxPrice.stripTrailingZeros().toPlainString());
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }
}
