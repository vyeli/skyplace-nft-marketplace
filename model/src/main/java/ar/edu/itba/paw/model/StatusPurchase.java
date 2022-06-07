package ar.edu.itba.paw.model;

public enum StatusPurchase {
    SUCCESS,
    CANCELLED;

    private final String statusName = toString();

    public String getStatusName() {
        return statusName;
    }
}
