package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SellOrderServiceImplTest {

    private static int ID_SELLORDER = 1;
    private static final Category VALID_CATEGORY = Category.Memes;
    private static final String VALID_CATEGORY_TEXT = VALID_CATEGORY.toString();
    private static final String INVALID_CATEGORY_TEXT = "MY_INVALID_CATEGORY";

    private final static int ID_OWNER = 1;
    private final static int ID_NFT = 1;
    private final static String NFT_CONTRACT_ADDR = "0xC37575F40b525FCA00554cB627CfFF8693d6b8B4";
    private final static String NFT_NAME = "Test nft";
    private final static Chain NFT_CHAIN = Chain.Cardano;
    private final static String NFT_CHAIN_TEXT = String.valueOf(NFT_CHAIN);
    private final static int ID_IMAGE_NFT = 1;
    private final static String NFT_COLLECTION = "Test nfts";
    private final static String NFT_DESCRIPTION = "A test nft";

    private final static int ID_USER1 = 1;
    private final static String MAIL_USER1 = "test1@test.test";
    private final static String USERNAME_USER1 = "test1";
    private final static String WALLET_USER1 = "0x3038BfE9acde2Fa667bEbF3322DBf9F33ca22c80";
    private final static Chain WALLETCHAIN_USER1 = Chain.Ethereum;
    private final static String PASSWORD_USER1 = "PASSUSER1";
    private final static Role ROLE_USER1 = Role.User;
    private final static String LOCALE_USER1 = "en";

    private final static int ID_USER2 = 2;
    private final static String MAIL_USER2 = "test2@test.test";
    private final static String USERNAME_USER2 = "test2";
    private final static String WALLET_USER2 = "0xE5Da717E6d0186bE40eEa87De0A5aC9d5c4e5666";
    private final static Chain WALLETCHAIN_USER2 = Chain.Ethereum;
    private final static String PASSWORD_USER2 = "PASSUSER2";
    private final static Role ROLE_USER2 = Role.User;
    private final static String LOCALE_USER2 = "en";

    private final static StatusBuyOrder DEFAULT_STATUS_BUY_ORDER = StatusBuyOrder.NEW;
    private final static Date PENDING_DATE_BUY_ORDER = null;

    private static BigDecimal price;

    @InjectMocks
    @Spy
    private SellOrderServiceImpl sellOrderService;

    @Mock
    private SellOrderDao sellOrderDao;

    @Mock
    private NftService nftService;

    @Mock
    private UserService userService;

    @Mock
    private ImageService imageService;

    @Mock
    private MailingService mailingService;

    @Before
    public void setUp(){
        price = new BigDecimal("0.001");
    }

    @Test(expected = UserIsNotNftOwnerException.class)
    public void testCreateSellOrderOnUnownedNft(){
        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(false);

        sellOrderService.create(price, ID_NFT, VALID_CATEGORY_TEXT);
    }

    @Test(expected = InvalidCategoryException.class)
    public void testCreateSellOrderWithInvalidCategory(){
        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(true);

        sellOrderService.create(price, ID_NFT, INVALID_CATEGORY_TEXT);
    }

    @Test(expected = NftNotFoundException.class)
    public void testCreateSellOrderOnInvalidNftId(){
        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(true);
        Mockito.when(nftService.getNFTById(ID_NFT)).thenReturn(Optional.empty());

        sellOrderService.create(price, ID_NFT, VALID_CATEGORY_TEXT);
    }

    @Test(expected = ImageNotFoundException.class)
    public void testCreateSellOrderOnInvalidNftImage(){
        User user = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, user);

        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(true);
        Mockito.when(nftService.getNFTById(ID_NFT)).thenReturn(Optional.of(nft));
        Mockito.when(imageService.getImage(ID_IMAGE_NFT)).thenReturn(Optional.empty());

        sellOrderService.create(price, ID_NFT, VALID_CATEGORY_TEXT);
    }

    @Test
    public void testCreateSellOrderOnNftWithValidData(){
        User user = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, user);
        byte[] imageBytes = new byte[]{10, 20, 30};
        Image image = new Image(ID_IMAGE_NFT, imageBytes);
        Locale userLocale = new Locale(LOCALE_USER1);

        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(true);
        Mockito.when(nftService.getNFTById(ID_NFT)).thenReturn(Optional.of(nft));
        Mockito.when(imageService.getImage(ID_IMAGE_NFT)).thenReturn(Optional.of(image));
        Mockito.doNothing().when(mailingService).sendNftSellOrderCreatedMail(MAIL_USER1, USERNAME_USER1, ID_NFT, NFT_NAME, NFT_CONTRACT_ADDR, price, imageBytes, userLocale);

        sellOrderService.create(price, ID_NFT, VALID_CATEGORY_TEXT);
    }

    @Test(expected = UserNoPermissionException.class)
    public void testUpdateSellOrderOnUnownedNft(){
        Mockito.doReturn(false).when(sellOrderService).currentUserOwnsSellOrder(ID_SELLORDER);

        sellOrderService.update(ID_SELLORDER, VALID_CATEGORY_TEXT, price);
    }

    @Test(expected = InvalidCategoryException.class)
    public void testUpdateSellOrderOnInvalidCategory(){
        Mockito.doReturn(true).when(sellOrderService).currentUserOwnsSellOrder(ID_SELLORDER);

        sellOrderService.update(ID_SELLORDER, INVALID_CATEGORY_TEXT, price);
    }


    @Test
    public void testUpdateSellOrderOnSuccessfulAttempt(){
        Mockito.doReturn(true).when(sellOrderService).currentUserOwnsSellOrder(ID_SELLORDER);
        Mockito.doReturn(Optional.of(new SellOrder(null, null, null))).when(sellOrderService).getOrderById(ID_SELLORDER);
        Mockito.doReturn(true).when(sellOrderService).sellOrderHasPendingBuyOrder(ID_SELLORDER);
        Mockito.when(sellOrderDao.update(ID_SELLORDER, Category.valueOf(VALID_CATEGORY_TEXT), price)).thenReturn(true);

        boolean result = sellOrderService.update(ID_SELLORDER, VALID_CATEGORY_TEXT, price);

        assertTrue(result);
    }

    @Test(expected = UserNoPermissionException.class)
    public void testDeleteSellOrderByUserWithoutPermission(){


        Mockito.doReturn(false).when(sellOrderService).hasPermission(ID_SELLORDER);

        sellOrderService.delete(ID_SELLORDER);
    }

    @Test(expected = ImageNotFoundException.class)
    public void testDeleteSellOrderOnNftWithoutImage(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
        SellOrder sellOrder = new SellOrder(price, nft, Category.valueOf(VALID_CATEGORY_TEXT));

        Mockito.when(imageService.getImage(ID_IMAGE_NFT)).thenReturn(Optional.empty());

        sellOrderService.delete(sellOrder);
    }

    @Test
    public void testDeleteSellOrderOnValidData(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
        SellOrder sellOrder = new SellOrder(price, nft, Category.valueOf(VALID_CATEGORY_TEXT));
        byte[] imageBytes = new byte[]{10, 20, 30};
        Image image = new Image(ID_IMAGE_NFT, imageBytes);
        Locale sellerLocale = new Locale(LOCALE_USER2);

        Mockito.when(imageService.getImage(ID_IMAGE_NFT)).thenReturn(Optional.of(image));
        Mockito.doNothing().when(mailingService).sendSellOrderDeletedMail(MAIL_USER2, USERNAME_USER2, ID_NFT, NFT_NAME, NFT_CONTRACT_ADDR, imageBytes, price, sellerLocale);

        sellOrderService.delete(sellOrder);
    }

    @Test(expected = UserNotLoggedInException.class)
    public void testDeleteBuyOrderWithUserNotLoggedIn(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
        SellOrder sellOrder = new SellOrder(price, nft, VALID_CATEGORY);
        BuyOrder buyOrder = new BuyOrder(price, sellOrder, bidder, DEFAULT_STATUS_BUY_ORDER, PENDING_DATE_BUY_ORDER);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());

        sellOrderService.delete(ID_SELLORDER, buyOrder);
    }

    @Test(expected = UserNoPermissionException.class)
    public void testDeleteBuyOrderOnUserWithoutPermissionAndNotCurrentUser(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
        SellOrder sellOrder = new SellOrder(price, nft, VALID_CATEGORY);
        BuyOrder buyOrder = new BuyOrder(price, sellOrder, bidder, DEFAULT_STATUS_BUY_ORDER, PENDING_DATE_BUY_ORDER);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(seller));
        Mockito.doReturn(false).when(sellOrderService).hasPermission(ID_SELLORDER);

        sellOrderService.delete(ID_SELLORDER, buyOrder);
    }

    @Test(expected = UserNoPermissionException.class)
    public void testDeleteBuyOrderWithoutPermissionAndNullBuyOrderSent(){
        User bidder = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(bidder));
        Mockito.doReturn(false).when(sellOrderService).hasPermission(ID_SELLORDER);

        sellOrderService.delete(ID_SELLORDER, null);
    }

    @Test(expected = UserNotLoggedInException.class)
    public void testCurrentUserOwnsSellOrderOnUserNotLoggedIn(){
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());

        sellOrderService.currentUserOwnsSellOrder(ID_SELLORDER);
    }

}
