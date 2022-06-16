package ar.edu.itba.paw.model;

public enum StatusPurchase {
    SUCCESS,
    CANCELLED;

    public String getStatusName() {
        return this.toString();
    }
}
