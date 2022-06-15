package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.PriceForm;
import ar.edu.itba.paw.webapp.form.SellNftForm;
import ar.edu.itba.paw.webapp.helpers.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.helpers.Utils.*;

// FIXME: Agregar chequeos de ser dueño de la oferta, pending, etc. por si bypasean el frontend, principalmente en /product/id
@Controller
public class ProductController {

    private final NftService nftService;
    private final UserService userService;
    private final SellOrderService sellOrderService;
    private final FavoriteService favoriteService;
    private final BuyOrderService buyOrderService;

    @Autowired
    public ProductController(NftService nftService, UserService userService, SellOrderService sellOrderService, FavoriteService favoriteService, BuyOrderService buyOrderService) {
        this.nftService = nftService;
        this.userService = userService;
        this.sellOrderService = sellOrderService;
        this.favoriteService = favoriteService;
        this.buyOrderService = buyOrderService;
    }

    @RequestMapping(value = "/sell/{productId:\\d+}", method = RequestMethod.GET)
    public ModelAndView createSellOrderForm(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable int productId) {

        final ModelAndView mav = new ModelAndView("frontcontroller/sell");
        Nft nft = nftService.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        if (nft.getOwner().getId() != user.getId())
            throw new UserIsNotNftOwnerException();
        mav.addObject("nft", nft);

        List<String> categories = Category.getCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    @RequestMapping(value = "/sell/{productId:\\d+}", method = RequestMethod.POST)
    public ModelAndView createSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable int productId) {
        if(errors.hasErrors())
            return createSellOrderForm(form, productId);

        SellOrder sellOrder = sellOrderService.create(form.getPrice(), productId, form.getCategory());
        return new ModelAndView("redirect:/product/" + sellOrder.getNft().getId());
    }

    @RequestMapping(value = "/sellOrder/{productId:\\d+}/update", method = RequestMethod.GET)
    public ModelAndView getUpdateSellOrder(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable int productId) {
        if (!userService.currentUserOwnsNft(productId))
            throw new UserNoPermissionException();

        final ModelAndView mav = new ModelAndView("frontcontroller/updateSellOrder");
        List<String> categories = Category.getCategories();
        mav.addObject("categories", categories);
        Nft nft = nftService.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        mav.addObject(  "nft", nft);
        SellOrder order = sellOrderService.getOrderById(nft.getSellOrder().getId()).orElseThrow(SellOrderNotFoundException::new);
        mav.addObject("order",order);
        return mav;
    }

    @RequestMapping(value = "/sellOrder/{productId:\\d+}/update", method = RequestMethod.POST)
    public ModelAndView updateSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable int productId) {
        if (errors.hasErrors())
            return getUpdateSellOrder(form, productId);

        Nft nft = nftService.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        boolean updated = sellOrderService.update(nft.getSellOrder().getId(), form.getCategory(), form.getPrice());
        return updated ? new ModelAndView("redirect:/product/" + nft.getId()) : getUpdateSellOrder(form, productId);
    }

    @RequestMapping(value = "/sellOrder/{productId:\\d+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteSellOrder(@PathVariable int productId) {
        Nft nft = nftService.getNFTById(productId).orElseThrow(NftNotFoundException::new);

        sellOrderService.delete(nft.getSellOrder().getId());
        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value = "/product/{productId:\\d+}", method = RequestMethod.GET)
    public ModelAndView product(@ModelAttribute("buyNftForm") final PriceForm form, @PathVariable int productId, @RequestParam(value = "offerPage", required = false) Integer offerPage, HttpServletRequest request, @RequestParam(value = "alert", required = false) String alert) {
        setEncodingToUTF(request);

        final ModelAndView mav = new ModelAndView("frontcontroller/product");
        mav.addObject("showOfferTab", offerPage != null);

        offerPage = offerPage == null ? 1 : offerPage;

        Nft nft = nftService.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        User owner = userService.getUserById(nft.getOwner().getId()).orElseThrow(UserNotFoundException::new);
        Optional<User> currentUser = userService.getCurrentUser();
        List<BuyOrder> buyOffers = new ArrayList<>();
        List<Publication> recommended = nftService.getRecommended(productId);
        mav.addObject("recommendedList", recommended);
        long amountOfferPages = 0;

        if(nft.getSellOrder() != null) {
            SellOrder sellOrder = nft.getSellOrder();
            Optional<BuyOrder> pendingBuyOrder = buyOrderService.getPendingBuyOrder(sellOrder.getId());
            mav.addObject("isBuyPending", pendingBuyOrder.isPresent());
            pendingBuyOrder.ifPresent(buyOrder -> mav.addObject("pendingBuyOrder", buyOrder));
            buyOffers = buyOrderService.getOrdersBySellOrderId(offerPage, sellOrder.getId());
            amountOfferPages = buyOrderService.getAmountPagesBySellOrderId(sellOrder);
            mav.addObject("sellOrder", sellOrder);
        }

        int favorites = favoriteService.getNftFavorites(productId);
        boolean isFaved = false;

        if(currentUser.isPresent()) {
            isFaved = favoriteService.isNftFavedByUser(currentUser.get().getId(), productId);
            mav.addObject("isAdmin", userService.isAdmin());
            mav.addObject("currentUser", currentUser.get());
        }

        mav.addObject("favorites", favorites);
        mav.addObject("nft", nft);
        mav.addObject("isFaved", isFaved);
        mav.addObject("offerPage", offerPage);
        mav.addObject("amountOfferPages", amountOfferPages);
        mav.addObject("buyOffer", buyOffers);
        mav.addObject("owner", owner);
        mav.addObject("productId", productId);
        mav.addObject("alert", Alert.getAlert(alert));
        return mav;
    }

    @RequestMapping(value = "/product/{productId:\\d+}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("buyNftForm") final PriceForm form, final BindingResult errors, @PathVariable int productId, @RequestParam(value = "offerPage", required = false) Integer offerPage, HttpServletRequest request) {
        setEncodingToUTF(request);
        if (errors.hasErrors())
            return product(form, productId, offerPage, request, null);

        Nft nft = nftService.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        SellOrder sellOrder = sellOrderService.getOrderById(nft.getSellOrder().getId()).orElseThrow(SellOrderNotFoundException::new);
        User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        buyOrderService.create(sellOrder.getId(), form.getPrice(), currentUser.getId());

        return product(form, productId, offerPage, request, null);
    }

    @RequestMapping(value = "/product/{productId:\\d+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNft(@PathVariable int productId) {
        Nft nft = nftService.getNFTById(productId).orElseThrow(NftNotFoundException::new);
        nftService.delete(nft);
        return new ModelAndView("redirect:/explore");
    }

    @RequestMapping(value="/buyorder/accept", method = RequestMethod.POST)
    public ModelAndView acceptBuyOrder(@RequestParam(value = "sellOrderId") int sellOrderId, @RequestParam(value = "buyerId") int buyerId, @RequestParam(value = "productId") int productId) {
        buyOrderService.acceptBuyOrder(sellOrderId, buyerId);
        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value="/buyorder/delete", method = RequestMethod.POST)
    public ModelAndView deleteBuyOrder(@RequestParam(value = "sellOrderId") int sellOrderId, @RequestParam(value = "buyerId") int buyerId, HttpServletRequest request) {
        buyOrderService.deleteBuyOrder(sellOrderId, buyerId);
        String referer = request.getHeader("Referer");
        return new ModelAndView("redirect:"+ referer);
    }

    @RequestMapping(value = "/buyorder/validate", method = RequestMethod.POST)
    public ModelAndView validateTransaction(@RequestParam(value="productId") int productId, @RequestParam(value = "transactionHash") String txHash, @RequestParam(value = "sellOrderId") int sellOrderId, @RequestParam(value = "buyerId") int buyerId) {
        // TODO: show if failed to user
        boolean validated = buyOrderService.validateTransaction(txHash, sellOrderId, buyerId);
        Alert returnAlert = validated ? Alert.SUCCESS : Alert.FAILURE;
        return new ModelAndView("redirect:/product/" + productId + "?alert=" + returnAlert.getName());
    }

}
