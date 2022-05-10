package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> create(String email, String username, String wallet, String walletChain, String password);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(int id);

    boolean userOwnsNft(int productId, User user);

    boolean userOwnsSellOrder(int productId, User user);

}
