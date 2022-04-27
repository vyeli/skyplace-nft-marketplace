package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.SellOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SellOrderServiceImpl implements SellOrderService {

    private final SellOrderDao sellOrderDao;
    private final UserService userService;

    @Autowired
    public SellOrderServiceImpl(SellOrderDao sellOrderDao, UserService userService) {
        this.sellOrderDao = sellOrderDao;
        this.userService = userService;
    }

    @Override
    public Optional<SellOrder> getOrderById(long id) {
        return sellOrderDao.getOrderById(id);
    }

    @Override
    public List<NftCard> getUserSellOrders(User user) {
        return sellOrderDao.getUserSellOrders(user);
    }

    @Override
    public SellOrder create(String name, int nftId, String nftContract, String chain, String category, BigDecimal price, String description, MultipartFile image, String email) {
        return sellOrderDao.create(name, nftId, nftContract, chain, category, price, description, image, email);
    }

    @Override
    public boolean update(long id, String category, BigDecimal price, String description) {
        if (!isUserOwner(id))
            return false;
        return sellOrderDao.update(id, category, price, description);
    }

    @Override
    public boolean delete(long id) {
        if (!isUserOwner(id))
            return false;
        return sellOrderDao.delete(id);
    }

    @Override
    public boolean isUserOwner(long sellOrderId) {
        Optional<SellOrder> maybeOrder = getOrderById(sellOrderId);
        String currentUserEmail = userService.getCurrentUser().getEmail();
        return maybeOrder.isPresent() && Objects.equals(currentUserEmail, maybeOrder.get().getSellerEmail());
    }

    @Override
    public List<NftCard> getUserFavorites(User user) {
        return sellOrderDao.getUserFavorites(user);
    }
}