package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {
    User create(String email, String username, String wallet, String password);

    Optional<User> getUserByEmail(String email);

    void setCurrentUser(User user);

    User getCurrentUser();
}
