package ar.edu.itba.paw.webapp.form;

public class ProfileFilter {
    private String page = "1";

    private String sort = "Name";

    private String items = "all";

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
