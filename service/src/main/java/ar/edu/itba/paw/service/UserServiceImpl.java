package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.exceptions.UserNotLoggedInException;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final MailingService mailingService;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final MailingService mailingService, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.mailingService = mailingService;
    }

    @Override
    public Optional<User> create(String email, String username, String wallet, String walletChain, String password) {
        if(userDao.getUserByEmail(email).isPresent())
            throw new UserAlreadyExistsException();
        Optional<User> user = userDao.create(email, username, wallet, walletChain, passwordEncoder.encode(password));
        if(user.isPresent())
            mailingService.sendRegisterMail(email, username, LocaleContextHolder.getLocale());
        return user;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails)
            return getUserByEmail(((UserDetails) principal).getUsername());
        return Optional.empty();
    }

    @Override
    public boolean userOwnsNft(int productId, User user) {
        return userDao.userOwnsNft(productId, user);
    }

    @Override
    public boolean currentUserOwnsNft(int productId) {
        User currentUser = getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        return userOwnsNft(productId, currentUser);
    }

    @Override
    public boolean userOwnsSellOrder(int productId, User user) {
        return userDao.userOwnsSellOrder(productId, user);
    }

    @Override
    public boolean currentUserOwnsSellOrder(int productId) {
        User currentUser = getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        return userOwnsSellOrder(productId, currentUser);
    }

    @Override
    public boolean isAdmin(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }

}
