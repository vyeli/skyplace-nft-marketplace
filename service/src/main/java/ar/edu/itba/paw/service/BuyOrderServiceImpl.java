package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BuyOrderServiceImpl implements BuyOrderService {
    private final BuyOrderDao buyOrderDao;
    private final UserService userService;
    private final SellOrderDao sellOrderDao;
    private final NftDao nftDao;
    private final ImageDao imageDao;
    private final MailingService mailingService;
    private final PurchaseService purchaseService;

    @Autowired
    public BuyOrderServiceImpl(BuyOrderDao  buyOrderDao, UserService userService, SellOrderDao sellOrderDao, NftDao nftDao, ImageDao imageDao, MailingService mailingService, PurchaseService purchaseService) {
        this.buyOrderDao = buyOrderDao;
        this.userService = userService;
        this.sellOrderDao = sellOrderDao;
        this.nftDao = nftDao;
        this.imageDao = imageDao;
        this.mailingService = mailingService;
        this.purchaseService = purchaseService;
    }

    @Override
    public boolean create(int idSellOrder, BigDecimal price, int userId) {
        if(userService.currentUserOwnsSellOrder(idSellOrder))
            throw new UserNoPermissionException();

        if(buyOrderDao.create(idSellOrder, price, userId)){
            BuyOrder buyOrder = new BuyOrder(idSellOrder, price, userId);
            SellOrder sellOrder = sellOrderDao.getOrderById(idSellOrder).orElseThrow(SellOrderNotFoundException::new);
            Nft nft = nftDao.getNFTById(sellOrder.getNftId()).orElseThrow(NftNotFoundException::new);
            User seller = userService.getUserById(nft.getIdOwner()).orElseThrow(UserNotFoundException::new);
            User bidder = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
            Image image = imageDao.getImage(nft.getIdImage()).orElseThrow(ImageNotFoundException::new);
            mailingService.sendOfferMail(bidder.getEmail(), seller.getEmail(), nft.getNftName(), nft.getId(), nft.getContractAddr(), buyOrder.getAmount(), image.getImage());
            return true;
        }
        return false;
    }

    @Override
    public List<BuyOffer> getOrdersBySellOrderId(int offerPage, int idSellOrder) {
        List<BuyOrder> buyOrders = buyOrderDao.getOrdersBySellOrderId(offerPage, idSellOrder);
        List<BuyOffer> buyOffers = new ArrayList<>();
        buyOrders.forEach(buyOrder -> {
            Optional<User> user = userService.getUserById(buyOrder.getIdBuyer());
            user.ifPresent(value -> buyOffers.add(new BuyOffer(buyOrder, value)));
        });
        return buyOffers;
    }

    @Override
    public int getAmountPagesBySellOrderId(int idSellOrder) {
        return buyOrderDao.getAmountPagesBySellOrderId(idSellOrder);
    }

    @Transactional
    @Override
    public void confirmBuyOrder(int sellOrderId, int buyerId, int seller, int productId, BigDecimal price) {
        if (!userService.currentUserOwnsNft(productId))
            throw new UserIsNotNftOwnerException();

        SellOrder sOrder = sellOrderDao.getOrderById(sellOrderId).orElseThrow(SellOrderNotFoundException::new);
        User buyerUser = userService.getUserById(buyerId).orElseThrow(UserNotFoundException::new);

        nftDao.updateOwner(sOrder.getNftId(), buyerUser.getId());
        sellOrderDao.delete(sOrder.getId());
        purchaseService.createPurchase(buyerId, seller, productId, price);
    }

    @Override
    public void deleteBuyOrder(int sellOrderId, int buyerId) {
        if (!userService.currentUserOwnsSellOrder(sellOrderId))
            throw new UserIsNotNftOwnerException();

        buyOrderDao.deleteBuyOrder(sellOrderId, buyerId);
    }
}
