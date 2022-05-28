/*
package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final int ID_USER = 1;
    private static final String MAIL_USER = "test@test.test";
    private static final String USERNAME_USER = "test1";
    private static final String WALLET_USER = "0x3038BfE9acde2Fa667bEbF3322DBf9F33ca22c80";;
    private static final String WALLETCHAIN_USER = "Ethereum";
    private static final String PASSWORD_USER = "password";
    private static final String ROLE_USER = "User";
    private static final int ID_NFT = 1;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailingService mailingService;

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateUserWithExistentUserMail(){
        User user = new User(ID_USER, MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER, PASSWORD_USER, ROLE_USER);
        Mockito.when(userDao.getUserByEmail(MAIL_USER)).thenReturn(Optional.of(user));

        userService.create(MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER, PASSWORD_USER);
    }

    @Test
    public void testCreateUserWithUnexistentUserMail(){
        User user = new User(ID_USER, MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER, PASSWORD_USER, ROLE_USER);
        Mockito.when(userDao.getUserByEmail(MAIL_USER)).thenReturn(Optional.empty());
        Mockito.when(userDao.create(MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER, PASSWORD_USER)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(PASSWORD_USER)).thenReturn(PASSWORD_USER);

        User finalUser = userService.create(MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER, PASSWORD_USER);

        assertEquals(ID_USER, finalUser.getId());
        assertEquals(MAIL_USER, finalUser.getEmail());
    }

}
*/