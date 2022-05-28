package ar.edu.itba.paw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Category {
    Collectible,
    Utility,
    Gaming,
    Sports,
    Music,
    VR,
    Memes,
    Photography,
    Miscellaneous,
    Art;

    private static final List<String> categories = Arrays.stream(Category.values()).map(Enum::name).collect(Collectors.toList());

    public static List<String> getCategories() {
        return categories;
    }
}

