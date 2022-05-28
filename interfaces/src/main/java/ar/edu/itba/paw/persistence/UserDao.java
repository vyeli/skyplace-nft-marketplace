package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Chain;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface UserDao {

    User create(String email, String username, String wallet, Chain walletChain, String password);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(int id);

    User merge(User user);

}
