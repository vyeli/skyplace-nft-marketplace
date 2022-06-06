package ar.edu.itba.paw.model;

public enum StatusPurchase {
    SUCCESS,
    CANCELLED;

    private String statusName = this.name();

    public String getStatusName() {
        return statusName;
    }
}
