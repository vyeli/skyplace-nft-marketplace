package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.BuyOffer;
import ar.edu.itba.paw.model.BuyOrder;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.BuyOrderDao;
import ar.edu.itba.paw.persistence.ImageDao;
import ar.edu.itba.paw.persistence.NftDao;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BuyOrderServiceImplTest {

    private final static int ID_SELLORDER = 1;

    private final static int ID_USER1 = 1;
    private final static String MAIL_USER1 = "test1@test.test";
    private final static String USERNAME_USER1 = "test1";
    private final static String WALLET_USER1 = "0x3038BfE9acde2Fa667bEbF3322DBf9F33ca22c80";
    private final static String WALLETCHAIN_USER1 = "Ethereum";
    private final static String PASSWORD_USER1 = "PASSUSER1";
    private final static String ROLE_USER1 = "User";

    private final static int ID_USER2 = 2;
    private final static String MAIL_USER2 = "test2@test.test";
    private final static String USERNAME_USER2 = "test2";
    private final static String WALLET_USER2 = "0xE5Da717E6d0186bE40eEa87De0A5aC9d5c4e5666";
    private final static String WALLETCHAIN_USER2 = "Ethereum";
    private final static String PASSWORD_USER2 = "PASSUSER2";
    private final static String ROLE_USER2 = "User";

    private final static int ID_BUYER = 1;
    private final static int ID_SELLER = 2;
    private final static int ID_NFT = 1;
    private final static int PAGE_NUM = 1;
    private final static String CATEGORY = "Other";

    private static BigDecimal testPrice;

    @InjectMocks
    private BuyOrderServiceImpl buyOrderService;

    @Mock
    private BuyOrderDao buyOrderDao;
    @Mock
    private UserService userService;
    @Mock
    private SellOrderDao sellOrderDao;
    @Mock
    private NftDao nftDao;
    @Mock
    private ImageDao imageDao;
    @Mock
    private MailingService mailingService;
    @Mock
    private PurchaseService purchaseService;

    @Before
    public void setUp(){
        testPrice = new BigDecimal("0.001");
    }

    @Test(expected = UserNoPermissionException.class)
    public void testCreateBuyOrderOnOwnedSellOrder() {
        Mockito.when(userService.currentUserOwnsSellOrder(ID_SELLORDER)).thenReturn(true);
        buyOrderService.create(ID_SELLORDER, testPrice, ID_USER1);
    }

    @Test
    public void testCreateBuyOrderWithExistentBuyOrderData() {
        Mockito.when(userService.currentUserOwnsSellOrder(ID_SELLORDER)).thenReturn(false);
        Mockito.when(buyOrderDao.create(ID_SELLORDER, testPrice, ID_USER1)).thenReturn(false);

        boolean createResult = buyOrderService.create(ID_SELLORDER, testPrice, ID_USER1);
        assertFalse(createResult);
    }

    @Test(expected = SellOrderNotFoundException.class)
    public void testCreateBuyOrderWithUnexistentSellOrderData() {
        Mockito.when(userService.currentUserOwnsSellOrder(ID_SELLORDER)).thenReturn(false);
        Mockito.when(buyOrderDao.create(ID_SELLORDER, testPrice, ID_USER1)).thenReturn(true);
        Mockito.when(sellOrderDao.getOrderById(ID_SELLORDER)).thenReturn(Optional.empty());

        buyOrderService.create(ID_SELLORDER, testPrice, ID_USER1);
    }

    @Test(expected = NftNotFoundException.class)
    public void testCreateBuyOrderWithUnexistentNftData() {
        SellOrder sellOrder = new SellOrder(ID_SELLORDER, testPrice, ID_NFT, CATEGORY);
        Mockito.when(userService.currentUserOwnsSellOrder(ID_SELLORDER)).thenReturn(false);
        Mockito.when(buyOrderDao.create(ID_SELLORDER, testPrice, ID_USER1)).thenReturn(true);
        Mockito.when(sellOrderDao.getOrderById(ID_SELLORDER)).thenReturn(Optional.of(sellOrder));
        Mockito.when(nftDao.getNFTById(ID_NFT)).thenReturn(Optional.empty());

        buyOrderService.create(ID_SELLORDER, testPrice, ID_USER1);
    }

    @Test
    public void testGetBuyOffersForUnexistentSellOrderId(){
        List<BuyOrder> buyOrders = new ArrayList<>();
        Mockito.when(buyOrderDao.getOrdersBySellOrderId(PAGE_NUM, ID_SELLORDER, buyOrderService.getPageSize())).thenReturn(buyOrders);

        int buyOfferAmount = buyOrderService.getOrdersBySellOrderId(PAGE_NUM, ID_SELLORDER).size();
        assertEquals(0, buyOfferAmount);
    }

    @Test
    public void testGetBuyOffersForExistentSellOrderId(){
        BuyOrder buyOrder1 = new BuyOrder(ID_SELLORDER, testPrice, ID_USER1);
        BuyOrder buyOrder2 = new BuyOrder(ID_SELLORDER, testPrice, ID_USER2);
        User user1 = new User(ID_USER1, MAIL_USER1, USERNAME_USER1, WALLET_USER1, WALLETCHAIN_USER1, PASSWORD_USER1, ROLE_USER1);
        User user2 = new User(ID_USER2, MAIL_USER2, USERNAME_USER2, WALLET_USER2, WALLETCHAIN_USER2, PASSWORD_USER2, ROLE_USER2);
        List<BuyOrder> buyOrders = new ArrayList<>();
        BuyOffer buyOffer1 = new BuyOffer(buyOrder1, user1);
        BuyOffer buyOffer2 = new BuyOffer(buyOrder2, user2);
        buyOrders.add(buyOrder1);
        buyOrders.add(buyOrder2);
        Mockito.when(buyOrderDao.getOrdersBySellOrderId(PAGE_NUM, ID_SELLORDER, buyOrderService.getPageSize())).thenReturn(buyOrders);
        Mockito.when(userService.getUserById(ID_USER1)).thenReturn(Optional.of(user1));
        Mockito.when(userService.getUserById(ID_USER2)).thenReturn(Optional.of(user2));

        List<BuyOffer> buyOfferList = buyOrderService.getOrdersBySellOrderId(PAGE_NUM, ID_SELLORDER);

        assertEquals(buyOfferList.get(0).getBuyOrder().getIdSellOrder(), buyOffer1.getBuyOrder().getIdSellOrder());
        assertEquals(buyOfferList.get(0).getBuyOrder().getIdBuyer(), buyOffer1.getBuyOrder().getIdBuyer());
        assertEquals(buyOfferList.get(1).getBuyOrder().getIdSellOrder(), buyOffer2.getBuyOrder().getIdSellOrder());
        assertEquals(buyOfferList.get(1).getBuyOrder().getIdBuyer(), buyOffer2.getBuyOrder().getIdBuyer());
    }

    @Test(expected = UserIsNotNftOwnerException.class)
    public void testConfirmBuyOrderOnUnownedNft(){
        Mockito.when(userService.currentUserOwnsNft(ID_SELLORDER)).thenReturn(false);
        buyOrderService.confirmBuyOrder(ID_SELLORDER, ID_BUYER, ID_SELLER, ID_NFT, testPrice);
    }

    @Test(expected = SellOrderNotFoundException.class)
    public void testConfirmBuyOrderOnUnexistentSellOrder(){
        Mockito.when(userService.currentUserOwnsNft(ID_SELLORDER)).thenReturn(true);
        Mockito.when(sellOrderDao.getOrderById(ID_SELLORDER)).thenReturn(Optional.empty());

        buyOrderService.confirmBuyOrder(ID_SELLORDER, ID_BUYER, ID_SELLER, ID_NFT, testPrice);
    }

    @Test(expected = UserNotFoundException.class)
    public void testConfirmBuyOrderWithUnexistentBuyer(){
        SellOrder sellOrder = new SellOrder(ID_SELLORDER, testPrice, ID_NFT, CATEGORY);
        Mockito.when(userService.currentUserOwnsNft(ID_SELLORDER)).thenReturn(true);
        Mockito.when(sellOrderDao.getOrderById(ID_SELLORDER)).thenReturn(Optional.of(sellOrder));
        Mockito.when(userService.getUserById(ID_BUYER)).thenReturn(Optional.empty());

        buyOrderService.confirmBuyOrder(ID_SELLORDER, ID_BUYER, ID_SELLER, ID_NFT, testPrice);
    }

    @Test
    public void testConfirmBuyOrderOnValidData(){
        SellOrder sellOrder = new SellOrder(ID_SELLORDER, testPrice, ID_NFT, CATEGORY);
        User buyerUser = new User(ID_USER1, MAIL_USER1, USERNAME_USER1, WALLET_USER1, WALLETCHAIN_USER1, PASSWORD_USER1, ROLE_USER1);
        Mockito.when(userService.currentUserOwnsNft(ID_SELLORDER)).thenReturn(true);
        Mockito.when(sellOrderDao.getOrderById(ID_SELLORDER)).thenReturn(Optional.of(sellOrder));
        Mockito.when(userService.getUserById(ID_BUYER)).thenReturn(Optional.of(buyerUser));
        
        buyOrderService.confirmBuyOrder(ID_SELLORDER, ID_BUYER, ID_SELLER, ID_NFT, testPrice);
    }

    @Test(expected = UserIsNotNftOwnerException.class)
    public void testDeleteBuyOrderOnUnownedSellOrder(){
        Mockito.when(userService.currentUserOwnsSellOrder(ID_NFT)).thenReturn(false);
        buyOrderService.deleteBuyOrder(ID_SELLORDER, ID_BUYER);
    }

    @Test
    public void testDeleteBuyOrderOnOwnedSellOrder() {
        Mockito.when(userService.currentUserOwnsSellOrder(ID_NFT)).thenReturn(true);
        buyOrderService.deleteBuyOrder(ID_SELLORDER, ID_BUYER);
    }
}
