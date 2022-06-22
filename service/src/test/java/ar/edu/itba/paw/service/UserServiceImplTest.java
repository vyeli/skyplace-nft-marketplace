package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserAlreadyExistsException;
import ar.edu.itba.paw.exceptions.UserNotLoggedInException;
import ar.edu.itba.paw.model.Chain;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.Optional;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private final static int ID_USER = 1;
    private final static String MAIL_USER = "test1@test.test";
    private final static String USERNAME_USER = "test1";
    private final static String WALLET_USER = "0x3038BfE9acde2Fa667bEbF3322DBf9F33ca22c80";
    private final static Chain WALLETCHAIN_USER = Chain.Ethereum;
    private final static String WALLETCHAIN_USER_TEXT = WALLETCHAIN_USER.toString();
    private final static String PASSWORD_USER = "PASSUSER1";
    private final static Role ROLE_USER = Role.User;
    private final static String LOCALE_USER = "en";

    private static final int ID_NFT = 1;

    @InjectMocks
    @Spy
    private UserServiceImpl userService;

    @Mock
    private UserDao userDao;

    @Mock
    private MailingService mailingService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test(expected = UserAlreadyExistsException.class)
    public void testCreateUserWithExistentUserMail(){
        User user = new User(ID_USER, USERNAME_USER, WALLET_USER, MAIL_USER, PASSWORD_USER, WALLETCHAIN_USER, ROLE_USER, LOCALE_USER);
        Mockito.when(userDao.getUserByEmail(MAIL_USER)).thenReturn(Optional.of(user));

        userService.create(MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER_TEXT, PASSWORD_USER);
    }

    @Test
    public void testCreateUserWithValidData(){
        User user = new User(ID_USER, USERNAME_USER, WALLET_USER, MAIL_USER, PASSWORD_USER, WALLETCHAIN_USER, ROLE_USER, LOCALE_USER);
        Locale locale = new Locale(LOCALE_USER);

        Mockito.when(userDao.getUserByEmail(MAIL_USER)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(PASSWORD_USER)).thenReturn("MY_PASSWORD_ENCODED");
        Mockito.when(userDao.create(MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER, "MY_PASSWORD_ENCODED", LOCALE_USER)).thenReturn(user);
        Mockito.doNothing().when(mailingService).sendRegisterMail(MAIL_USER, USERNAME_USER, locale);

        userService.create(MAIL_USER, USERNAME_USER, WALLET_USER, WALLETCHAIN_USER_TEXT, PASSWORD_USER);
    }

    @Test(expected = UserNotLoggedInException.class)
    public void testCurrentUserOwnsNftOnUserNotLoggedIn(){
        Mockito.doReturn(Optional.empty()).when(userService).getCurrentUser();

        userService.currentUserOwnsNft(ID_NFT);
    }

}
