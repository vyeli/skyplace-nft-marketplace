package ar.edu.itba.paw.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {
    User,
    Admin;

    private static final List<String> roles = Arrays.stream(Role.values()).map(Enum::name).collect(Collectors.toList());

    public static List<String> getRoles() {
        return roles;
    }
}
