package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserService {
    User create(String email, String username, String wallet, String walletChain, String password);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(int id);

    Optional<User> getCurrentUser();

    boolean userOwnsNft(int productId, User user);

    boolean currentUserOwnsNft(int productId);

    boolean isAdmin();
}
