package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

public class ExploreFilter {
    private String page = "1";

    private String search;

    private String searchFor = "nft";

    private String category;

    private String chain;

    private String sort = "Name";

    private String status = "";

    @DecimalMin(value="0")
    @Digits(integer=18, fraction=18)
    private BigDecimal minPrice = BigDecimal.ZERO;

    @DecimalMin(value="0")
    @Digits(integer=18, fraction=18)
    private BigDecimal maxPrice = BigDecimal.ZERO;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChain() {
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
        return minPrice == null ? BigDecimal.ZERO : minPrice.stripTrailingZeros();
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice == null ? BigDecimal.ZERO : maxPrice.stripTrailingZeros();
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(String searchFor) {
        this.searchFor = searchFor;
    }

}
