package ar.edu.itba.paw.webapp.helpers;

public class BuyOrderItemType {

    private final String name;
    private boolean isActive;

    public BuyOrderItemType(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
