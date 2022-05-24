package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserIsNotNftOwnerException;
import ar.edu.itba.paw.exceptions.UserNoPermissionException;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

@RunWith(MockitoJUnitRunner.class)
public class SellOrderServiceImplTest {

    private static final int ID_NFT = 1;
    private static final String CATEGORY = "Other";

    private static BigDecimal price;

    @InjectMocks
    private SellOrderServiceImpl sellOrderService;

    @Mock
    private SellOrderDao sellOrderDao;

    @Mock
    private UserService userService;

    @Before
    public void setUp(){
        price = new BigDecimal("0.001");
    }

    @Test(expected = UserIsNotNftOwnerException.class)
    public void testCreateSellOrderOnUnownedNft(){
        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(false);

        sellOrderService.create(price, ID_NFT, CATEGORY);
    }

    @Test
    public void testCreateSellOrderOnOwnedNft(){
        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(true);

        sellOrderService.create(price, ID_NFT, CATEGORY);
    }

    @Test(expected = UserNoPermissionException.class)
    public void testUpdateSellOrderOnUnownedNft(){
        Mockito.when(userService.currentUserOwnsSellOrder(ID_NFT)).thenReturn(false);

        sellOrderService.update(ID_NFT, CATEGORY, price);
    }

    @Test
    public void testUpdateSellOrderOnOwnedNft(){
        Mockito.when(userService.currentUserOwnsSellOrder(ID_NFT)).thenReturn(true);

        sellOrderService.update(ID_NFT, CATEGORY, price);
    }

    @Test(expected = UserNoPermissionException.class)
    public void testDeleteSellOrderOnUnownedNftOrNotAdmin(){
        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(false);
        Mockito.when(userService.isAdmin()).thenReturn(false);

        sellOrderService.update(ID_NFT, CATEGORY, price);
    }

    @Test
    public void testDeleteSellOrderOnOwnedNft(){
        Mockito.when(userService.currentUserOwnsNft(ID_NFT)).thenReturn(true);

        sellOrderService.delete(ID_NFT);
    }

    @Test
    public void testDeleteSellOrderOnAdminAccount(){
        Mockito.when(userService.isAdmin()).thenReturn(true);

        sellOrderService.delete(ID_NFT);
    }

}
