package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Chain;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class UserJpaDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String email, String username, String wallet, Chain walletChain, String password) {
        final User user = new User(username, wallet, email, password, walletChain, Role.User);
        em.persist(user);
        return user;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public User merge(User user) {
        return em.merge(user);
    }

}
