package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserIsNotNftOwnerException;
import ar.edu.itba.paw.persistence.NftDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class NftServiceImplTest {

    private static final int ID_PRODUCT = 1;

    @InjectMocks
    private NftServiceImpl nftService;

    @Mock
    private UserService userService;

    @Mock
    private NftDao nftDao;

    @Test(expected = UserIsNotNftOwnerException.class)
    public void testDeleteNftOnUserNotOwnerOrAdmin(){
        Mockito.when(userService.currentUserOwnsNft(ID_PRODUCT)).thenReturn(false);
        Mockito.when(userService.isAdmin()).thenReturn(false);

        nftService.delete(ID_PRODUCT);
    }

    @Test
    public void testDeleteNftOnUserOwner(){
        Mockito.when(userService.currentUserOwnsNft(ID_PRODUCT)).thenReturn(true);

        nftService.delete(ID_PRODUCT);
    }

    @Test
    public void testDeleteNftOnUserAdmin(){
        Mockito.when(userService.isAdmin()).thenReturn(true);

        nftService.delete(ID_PRODUCT);
    }

}
