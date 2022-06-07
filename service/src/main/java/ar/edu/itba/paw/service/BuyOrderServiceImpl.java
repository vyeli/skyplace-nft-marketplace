package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class BuyOrderServiceImpl implements BuyOrderService {
    private final BuyOrderDao buyOrderDao;
    private final UserService userService;
    private final SellOrderService sellOrderService;
    private final ImageService imageService;
    private final MailingService mailingService;
    private final PurchaseService purchaseService;
    private final EtherscanService etherscanService;

    private final int pageSize = 5;
    private static final long SECONDS_IN_ONE_DAY = 24*60*60;

    @Autowired
    public BuyOrderServiceImpl(BuyOrderDao buyOrderDao, UserService userService, SellOrderService sellOrderService, ImageService imageService, MailingService mailingService, PurchaseService purchaseService, EtherscanService etherscanService) {
        this.buyOrderDao = buyOrderDao;
        this.userService = userService;
        this.sellOrderService = sellOrderService;
        this.imageService = imageService;
        this.mailingService = mailingService;
        this.purchaseService = purchaseService;
        this.etherscanService = etherscanService;
    }

    @Transactional
    @Override
    public boolean create(int idSellOrder, BigDecimal price, int userId) {
        if(sellOrderService.currentUserOwnsSellOrder(idSellOrder))
            throw new UserNoPermissionException();
        
        SellOrder sellOrder = sellOrderService.getOrderById(idSellOrder).orElseThrow(SellOrderNotFoundException::new);
        User bidder = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        buyOrderDao.create(sellOrder, price, bidder);
        Nft nft = sellOrder.getNft();
        User seller = userService.getUserById(nft.getOwner().getId()).orElseThrow(UserNotFoundException::new);
        Image image = imageService.getImage(nft.getIdImage()).orElseThrow(ImageNotFoundException::new);
        mailingService.sendOfferMail(bidder.getEmail(), seller.getEmail(), nft.getNftName(), nft.getId(), nft.getContractAddr(), price, image.getImage(), LocaleContextHolder.getLocale());
        return true;
    }

    private void checkPendingOrdersDateBySellOrderId(int sellOrderId) {
        rejectBuyOrder(getPendingBuyOrder(sellOrderId));
    }

    private void checkPendingOrdersDateForUser(User user) {
        List<BuyOrder> buyOrders = buyOrderDao.getExpiredPendingOffersByUser(user);
        for(BuyOrder b:buyOrders)
            rejectBuyOrder(Optional.of(b));
    }

    @Transactional
    @Override
    public List<BuyOrder> getOrdersBySellOrderId(int page, int sellOrderId) {
        checkPendingOrdersDateBySellOrderId(sellOrderId);
        return buyOrderDao.getOrdersBySellOrderId(page, sellOrderId, getPageSize());
    }

    @Override
    public int getAmountPagesBySellOrderId(SellOrder sellOrder) {
        return (sellOrder.getBuyOrdersAmount() - 1) / getPageSize() + 1;
    }

    @Transactional
    @Override
    public List<BuyOrder> getBuyOrdersForUser(User user, int page) {
        checkPendingOrdersDateForUser(user);
        return buyOrderDao.getBuyOrdersForUser(user, page, getPageSize());
    }

    @Override
    public int getAmountPagesForUser(User user) {
        return (user.getBuyOrders().size() - 1) / getPageSize() + 1;
    }

    private boolean confirmBuyOrder(int sellOrderId, int buyerId, String txHash) {
        Optional<BuyOrder> buyOrder = buyOrderDao.getBuyOrder(sellOrderId, buyerId);
        if(!buyOrder.isPresent())
            return false;
        SellOrder sellOrder = buyOrder.get().getOfferedFor();
        User buyer = buyOrder.get().getOfferedBy();
        Nft nft = sellOrder.getNft();
        User seller = nft.getOwner();
        Image image = imageService.getImage(nft.getIdImage()).orElseThrow(ImageNotFoundException::new);

        sellOrder.getNft().setOwner(buyer);
        mailingService.sendOfferAcceptedMail(buyer.getEmail(), seller.getEmail(), seller.getId(), buyer.getUsername(), nft.getNftName(), nft.getNftId(), nft.getContractAddr(), buyOrder.get().getAmount(), image.getImage(), LocaleContextHolder.getLocale());

        sellOrderService.delete(sellOrder.getId());
        purchaseService.createPurchase(buyerId, seller.getId(), nft.getId(), buyOrder.get().getAmount(), txHash, StatusPurchase.SUCCESS);
        return true;
    }

    @Transactional
    @Override
    public void acceptBuyOrder(int sellOrderId, int buyerId) {
        buyOrderDao.acceptBuyOrder(sellOrderId, buyerId);
    }

    private void rejectBuyOrder(Optional<BuyOrder> buyOrder) {
        if(!buyOrder.isPresent() || buyOrder.get().getStatus().equals(StatusBuyOrder.NEW) || buyOrder.get().getPendingDate() == null)
            return;

        if(Instant.now().minusMillis(buyOrder.get().getPendingDate().getTime()).getEpochSecond() < SECONDS_IN_ONE_DAY)
            return;

        buyOrderDao.rejectBuyOrder(buyOrder.get().getOfferedFor().getId(), buyOrder.get().getOfferedBy().getId());
        Nft nft = buyOrder.get().getOfferedFor().getNft();
        User seller = buyOrder.get().getOfferedFor().getNft().getOwner();
        purchaseService.createPurchase(buyOrder.get().getOfferedBy().getId(), seller.getId(), nft.getId(), buyOrder.get().getAmount(), null, StatusPurchase.CANCELLED);
    }

    @Transactional
    @Override
    public void rejectBuyOrder(int sellOrderId, int buyerId) {
        rejectBuyOrder(buyOrderDao.getBuyOrder(sellOrderId, buyerId));
    }

    @Transactional
    @Override
    public void deleteBuyOrder(int sellOrderId, int buyerId) {
        if (!sellOrderService.currentUserOwnsSellOrder(sellOrderId) && buyerId != userService.getCurrentUser().get().getId())
            throw new UserIsNotNftOwnerException();

        SellOrder sellOrder = sellOrderService.getOrderById(sellOrderId).orElseThrow(SellOrderNotFoundException::new);
        User buyer = userService.getUserById(buyerId).orElseThrow(UserNotFoundException::new);
        Nft nft = sellOrder.getNft();
        User seller = userService.getUserById(nft.getOwner().getId()).orElseThrow(UserNotFoundException::new);
        Image image = imageService.getImage(nft.getIdImage()).orElseThrow(ImageNotFoundException::new);
        BigDecimal offerAmount = sellOrder.getBuyOrder(buyer).get().getAmount();
        sellOrder.deleteBuyOrder(buyer);
        mailingService.sendOfferRejectedMail(buyer.getEmail(), seller.getEmail(), buyer.getUsername(), nft.getNftName(), nft.getNftId(), nft.getContractAddr(), new BigDecimal(offerAmount.stripTrailingZeros()
                .toPlainString()), image.getImage(),  LocaleContextHolder.getLocale());
    }

    @Override
    public boolean sellOrderPendingBuyOrder(int sellOrderId) {
        return buyOrderDao.sellOrderPendingBuyOrder(sellOrderId);
    }

    @Override
    public Optional<BuyOrder> getPendingBuyOrder(int sellOrderId) {
        return buyOrderDao.getPendingBuyOrder(sellOrderId);
    }

    @Transactional
    @Override
    public boolean validateTransaction(String txHash, int sellOrderId, int buyerId) {
        if(purchaseService.isTxHashAlreadyInUse(txHash))
            return false;

        Optional<BuyOrder> buyOrder = getPendingBuyOrder(sellOrderId);
        if(!buyOrder.isPresent() || !buyOrder.get().getStatus().equals(StatusBuyOrder.PENDING))
            return false;
        User seller = buyOrder.get().getOfferedFor().getNft().getOwner();
        User buyer = userService.getUserById(buyerId).orElseThrow(UserNotFoundException::new);
        boolean isValid = etherscanService.isTransactionValid(txHash, buyer.getWallet(), seller.getWallet(), buyOrder.get().getAmount());
        System.out.println("isValid: "+isValid);
        if(!isValid)
            return false;

        return confirmBuyOrder(sellOrderId, buyerId, txHash);
    }

    public int getPageSize() {
        return pageSize;
    }


    @Override
    public boolean nftHasValidTransaction(int sellOrderId, int idBuyer, BigDecimal price, String txHash) {
        Optional<SellOrder> sellOrder = sellOrderService.getOrderById(sellOrderId);
        Optional<User> buyer = userService.getUserById(idBuyer);
        if(!sellOrder.isPresent() || !buyer.isPresent())
            return false;
        return etherscanService.isTransactionValid(txHash,buyer.get().getWallet(),sellOrder.get().getNft().getOwner().getWallet(),price);
    }
}
