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

    @RequestMapping(value = "/sell/{productId}", method = RequestMethod.GET)
    public ModelAndView createSellOrderForm(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable String productId) {
        int parsedProductId = parseInt(productId);

        final ModelAndView mav = new ModelAndView("frontcontroller/sell");
        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        if (nft.getOwner().getId() != user.getId())
            throw new UserIsNotNftOwnerException();
        mav.addObject("nft", nft);

        List<String> categories = Category.getCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    @RequestMapping(value = "/sell/{productId}", method = RequestMethod.POST)
    public ModelAndView createSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable String productId) {
        if(errors.hasErrors())
            return createSellOrderForm(form, productId);

        int parsedProductId = parseInt(productId);
        SellOrder sellOrder = sellOrderService.create(form.getPrice(), parsedProductId, form.getCategory());
        return new ModelAndView("redirect:/product/" + sellOrder.getNft().getId());
    }

    @RequestMapping(value = "/sellOrder/{productId}/update", method = RequestMethod.GET)
    public ModelAndView getUpdateSellOrder(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable String productId) {
        int parsedProductId = parseInt(productId);

        if (!userService.currentUserOwnsNft(parsedProductId))
            throw new UserNoPermissionException();

        final ModelAndView mav = new ModelAndView("frontcontroller/updateSellOrder");
        List<String> categories = Category.getCategories();
        mav.addObject("categories", categories);
        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        mav.addObject(  "nft", nft);
        SellOrder order = sellOrderService.getOrderById(nft.getSellOrder().getId()).orElseThrow(SellOrderNotFoundException::new);
        mav.addObject("order",order);
        return mav;
    }

    @RequestMapping(value = "/sellOrder/{productId}/update", method = RequestMethod.POST)
    public ModelAndView updateSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        if (errors.hasErrors())
            return getUpdateSellOrder(form, productId);

        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        boolean updated = sellOrderService.update(nft.getSellOrder().getId(), form.getCategory(), form.getPrice());
        return updated ? new ModelAndView("redirect:/product/" + nft.getId()) : getUpdateSellOrder(form, productId);
    }

    @RequestMapping(value = "/sellOrder/{productId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteSellOrder(@PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);

        sellOrderService.delete(nft.getSellOrder().getId());
        return new ModelAndView("redirect:/product/" + parsedProductId);
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ModelAndView product(@ModelAttribute("buyNftForm") final PriceForm form, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage, HttpServletRequest request, @RequestParam(value = "alert", required = false) String alert) {
        setEncodingToUTF(request);

        int parsedProductId = parseInt(productId);
        int parsedOfferPage = offerPage == null ? 1 : parseInt(offerPage);

        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        User owner = userService.getUserById(nft.getOwner().getId()).orElseThrow(UserNotFoundException::new);
        Optional<User> currentUser = userService.getCurrentUser();
        List<BuyOrder> buyOffers = new ArrayList<>();
        long amountOfferPages = 0;

        final ModelAndView mav = new ModelAndView("frontcontroller/product");

        if(nft.getSellOrder() != null) {
            SellOrder sellOrder = nft.getSellOrder();
            Optional<BuyOrder> pendingBuyOrder = buyOrderService.getPendingBuyOrder(sellOrder.getId());
            mav.addObject("isBuyPending", pendingBuyOrder.isPresent());
            pendingBuyOrder.ifPresent(buyOrder -> mav.addObject("pendingBuyOrder", buyOrder));
            buyOffers = buyOrderService.getOrdersBySellOrderId(parsedOfferPage, sellOrder.getId());
            amountOfferPages = buyOrderService.getAmountPagesBySellOrderId(sellOrder);
            mav.addObject("sellOrder", sellOrder);
        }

        int favorites = favoriteService.getNftFavorites(parsedProductId);
        boolean isFaved = false;

        if(currentUser.isPresent()) {
            isFaved = favoriteService.userFavedNft(currentUser.get().getId(), nft.getId());
            mav.addObject("isAdmin", userService.isAdmin());
            mav.addObject("currentUser", currentUser.get());
        }

        mav.addObject("favorites", favorites);
        mav.addObject("nft", nft);
        mav.addObject("isFaved", isFaved);
        mav.addObject("offerPage", parsedOfferPage);
        mav.addObject("showOfferTab", offerPage != null);
        mav.addObject("amountOfferPages", amountOfferPages);
        mav.addObject("buyOffer", buyOffers);
        mav.addObject("owner", owner);
        mav.addObject("productId", parsedProductId);
        mav.addObject("alert", Alert.getAlert(alert));
        return mav;
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("buyNftForm") final PriceForm form, final BindingResult errors, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage, HttpServletRequest request) {
        setEncodingToUTF(request);

        if (errors.hasErrors()) {
            return product(form, productId, offerPage, request, null);
        }
        int parsedProductId = parseInt(productId);

        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        SellOrder sellOrder = sellOrderService.getOrderById(nft.getSellOrder().getId()).orElseThrow(SellOrderNotFoundException::new);
        User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        buyOrderService.create(sellOrder.getId(), form.getPrice(), currentUser.getId());

        ModelAndView mav = product(form, productId, offerPage, request, null);
        mav.addObject("emailSent", true);
        return mav;
    }

    @RequestMapping(value = "/product/{productId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNft(@PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        nftService.delete(nft);
        return new ModelAndView("redirect:/explore");
    }

    @RequestMapping(value="/buyorder/confirm", method = RequestMethod.POST)
    @Deprecated
    public ModelAndView confirmBuyOrder(@RequestParam(value = "sellOrder") String sellOrderId, @RequestParam(value = "idBuyer") String buyerId, @RequestParam(value = "idNft") String productId, @RequestParam(value = "idSeller") String seller, @RequestParam(value = "price") BigDecimal price) {
        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value="/buyorder/accept", method = RequestMethod.POST)
    public ModelAndView acceptBuyOrder(@RequestParam(value = "sellOrder") String sellOrderId, @RequestParam(value = "idBuyer") String buyerId, @RequestParam(value = "idNft") String productId) {
        int parsedSellOrderId = parseInt(sellOrderId);
        int parsedBuyerId = parseInt(buyerId);
        buyOrderService.acceptBuyOrder(parsedSellOrderId, parsedBuyerId);
        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value="/buyorder/delete", method = RequestMethod.POST)
    public ModelAndView deleteBuyOrder(@RequestParam(value = "sellOrderId") String sellOrderId, @RequestParam(value = "buyerId") String buyerId, HttpServletRequest request) {
        int parsedSellOrderId = parseInt(sellOrderId);
        int parsedBuyerId = parseInt(buyerId);
        buyOrderService.deleteBuyOrder(parsedSellOrderId, parsedBuyerId);
        String referer = request.getHeader("Referer");
        return new ModelAndView("redirect:"+ referer);
    }

    @RequestMapping(value = "/buyorder/validate", method = RequestMethod.POST)
    public ModelAndView validateTransaction(@RequestParam(value="productId") String productId, @RequestParam(value = "transactionHash") String txHash, @RequestParam(value = "sellOrderId") String sellOrderId, @RequestParam(value = "buyerId") String buyerId) {
        int parsedSellOrderId = parseInt(sellOrderId);
        int parsedBuyerId = parseInt(buyerId);
        // TODO: show if failed to user
        boolean validated = buyOrderService.validateTransaction(txHash, parsedSellOrderId, parsedBuyerId);
        Alert returnAlert = validated ? Alert.SUCCESS : Alert.FAILURE;
        return new ModelAndView("redirect:/product/" + productId + "?alert=" + returnAlert.getName());
    }

}
