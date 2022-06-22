package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.PurchaseDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseServiceImplTest {

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

    private final static int ID_NFT = 1;
    private final static String NFT_CONTRACT_ADDR = "0xC37575F40b525FCA00554cB627CfFF8693d6b8B4";
    private final static String NFT_NAME = "Test nft";
    private final static Chain NFT_CHAIN = Chain.Cardano;
    private final static int ID_IMAGE_NFT = 1;
    private final static String NFT_COLLECTION = "Test nfts";
    private final static String NFT_DESCRIPTION = "A test nft";

    private final static int PAGE_NUM = 1;

    private final static int ID_PURCHASE = 1;
    private final static StatusPurchase STATUS_PURCHASE = StatusPurchase.SUCCESS;
    private final static Date DATE_PURCHASE = null;
    private final static String TX_HASH = "3E5XGTx46hAJYUNFZ5a8EhGZ7hXR8irenxTozSdZ84gq4N1hqvSGAtuvm9aTKGTRjaFbU1TDLtdQFg4GHK5XxiSi";

    private final static BigDecimal BUYORDER_PRICE = BigDecimal.ONE;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Mock
    private UserService userService;

    @Mock
    private NftService nftService;

    @Mock
    private PurchaseDao purchaseDao;

    @Test
    public void testGetAllTransactionsOnUserNotLoggedIn(){
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());

        List<Purchase> transactionList = purchaseService.getAllTransactions(ID_USER1, PAGE_NUM);

        assertEquals(0, transactionList.size());
    }

    @Test
    public void testGetAllTransactionsOnUserNotCurrentUser(){
        User currentUser = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(currentUser));

        List<Purchase> transactionList = purchaseService.getAllTransactions(ID_USER2, PAGE_NUM);

        assertEquals(0, transactionList.size());
    }

    @Test
    public void testCreatePurchaseOnInvalidBuyer(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);

        Mockito.when(userService.getUserById(ID_USER1)).thenReturn(Optional.empty());
        Mockito.when(userService.getUserById(ID_USER2)).thenReturn(Optional.of(seller));
        Mockito.when(nftService.getNFTById(ID_NFT)).thenReturn(Optional.of(nft));

        int result = purchaseService.createPurchase(ID_USER1, ID_USER2, ID_NFT, BUYORDER_PRICE, TX_HASH, STATUS_PURCHASE);

        assertEquals(0, result);
    }

    @Test
    public void testCreatePurchaseOnInvalidSeller(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);

        Mockito.when(userService.getUserById(ID_USER1)).thenReturn(Optional.of(bidder));
        Mockito.when(userService.getUserById(ID_USER2)).thenReturn(Optional.empty());
        Mockito.when(nftService.getNFTById(ID_NFT)).thenReturn(Optional.of(nft));

        int result = purchaseService.createPurchase(ID_USER1, ID_USER2, ID_NFT, BUYORDER_PRICE, TX_HASH, STATUS_PURCHASE);

        assertEquals(0, result);
    }

    @Test
    public void testCreatePurchaseOnInvalidNft(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);

        Mockito.when(userService.getUserById(ID_USER1)).thenReturn(Optional.of(bidder));
        Mockito.when(userService.getUserById(ID_USER2)).thenReturn(Optional.of(seller));
        Mockito.when(nftService.getNFTById(ID_NFT)).thenReturn(Optional.empty());

        int result = purchaseService.createPurchase(ID_USER1, ID_USER2, ID_NFT, BUYORDER_PRICE, TX_HASH, STATUS_PURCHASE);

        assertEquals(0, result);
    }

    @Test
    public void testCreatePurchaseWithValidData(){
        User seller = new User(ID_USER2, USERNAME_USER2, WALLET_USER2, MAIL_USER2, PASSWORD_USER2, WALLETCHAIN_USER2, ROLE_USER2, LOCALE_USER2);
        User bidder = new User(ID_USER1, USERNAME_USER1, WALLET_USER1, MAIL_USER1, PASSWORD_USER1, WALLETCHAIN_USER1, ROLE_USER1, LOCALE_USER1);
        Nft nft = new Nft(ID_NFT, NFT_CONTRACT_ADDR, NFT_NAME, NFT_CHAIN, ID_IMAGE_NFT, NFT_COLLECTION, NFT_DESCRIPTION, seller);
        Purchase purchase = new Purchase(ID_PURCHASE, BUYORDER_PRICE, DATE_PURCHASE, nft, bidder, seller, STATUS_PURCHASE, TX_HASH);

        Mockito.when(userService.getUserById(ID_USER1)).thenReturn(Optional.of(bidder));
        Mockito.when(userService.getUserById(ID_USER2)).thenReturn(Optional.of(seller));
        Mockito.when(nftService.getNFTById(ID_NFT)).thenReturn(Optional.of(nft));
        Mockito.when(purchaseDao.createPurchase(bidder, seller, nft, BUYORDER_PRICE, TX_HASH, STATUS_PURCHASE)).thenReturn(purchase);

        int result = purchaseService.createPurchase(ID_USER1, ID_USER2, ID_NFT, BUYORDER_PRICE, TX_HASH, STATUS_PURCHASE);

        assertNotEquals(0, result);
    }

}
