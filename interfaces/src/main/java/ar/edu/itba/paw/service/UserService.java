package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> create(String email, String username, String wallet, String password);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(long id);

    User getCurrentUser();
}
