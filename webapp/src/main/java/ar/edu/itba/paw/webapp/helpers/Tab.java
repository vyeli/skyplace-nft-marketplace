package ar.edu.itba.paw.webapp.helpers;

import org.jetbrains.annotations.NotNull;

public class Tab implements Comparable<Tab>{
    private final Integer tabIndex;
    private final String name;
    private final boolean isPublic;
    private boolean isActive;

    public Tab(Integer tabIndex, String name, boolean isPublic, boolean isActive) {
        this.tabIndex = tabIndex;
        this.name = name;
        this.isPublic = isPublic;
        this.isActive = isActive;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NotNull Tab o) {
        return tabIndex.compareTo(o.getTabIndex());
    }
}
