package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.exceptions.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class FrontController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);

    private final SellOrderService sellOrderService;
    private final CategoryService categoryService;
    private final ChainService chainService;
    private final UserService userService;
    private final ImageService imageService;
    private final NftService nftService;
    private final BuyOrderService buyOrderService;
    private final FavoriteService favoriteService;
    private final PurchaseService purchaseService;

    @Autowired
    public FrontController(SellOrderService sellOrderService, CategoryService categoryService, ChainService chainService, UserService userService, ImageService imageService, NftService nftService, BuyOrderService buyOrderService, FavoriteService favoriteService, PurchaseService purchaseService) {
        this.sellOrderService = sellOrderService;
        this.categoryService = categoryService;
        this.chainService = chainService;
        this.userService = userService;
        this.imageService = imageService;
        this.nftService = nftService;
        this.buyOrderService = buyOrderService;
        this.favoriteService = favoriteService;
        this.purchaseService = purchaseService;
    }

    @RequestMapping(value="/")
    public ModelAndView home() {
        return new ModelAndView("frontcontroller/index");
    }

    @RequestMapping("/explore")
    public ModelAndView explore(@ModelAttribute("exploreFilter") @Valid ExploreFilter exploreFilter) {

        List<String> categories = categoryService.getCategories();
        List<String> chains = chainService.getChains();

        final ModelAndView mav = new ModelAndView("frontcontroller/explore");

        final int parsedPage = parseInt(exploreFilter.getPage());
        final List<Publication> publications = nftService.getAllPublications(parsedPage, exploreFilter.getStatus(), exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(),  exploreFilter.getSearch());
        int publicationsAmount = 0;
        if(publications.isEmpty())
            mav.addObject("noPublication", true);
        else
            publicationsAmount = nftService.getAmountPublications(exploreFilter.getStatus(), exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSearch());

        String categoryFormat = "All";
        if(exploreFilter.getCategory() != null && !exploreFilter.getCategory().equals(""))
            if(exploreFilter.getCategory().contains(","))
                categoryFormat = "Various";
            else
                categoryFormat = capitalizeString(exploreFilter.getCategory());

        mav.addObject("category", categoryFormat);
        mav.addObject("publications", publications);
        mav.addObject("pages", publicationsAmount/12+1);
        mav.addObject("publicationsAmount", publicationsAmount);
        mav.addObject("categories", categories);
        mav.addObject("chains", chains);
        String sortFormat = "Name";
        switch(exploreFilter.getSort()) {
            case "priceAsc":
                sortFormat = "Price Ascending";
                break;
            case "priceDsc":
                sortFormat = "Price Descending";
                break;
        }
        mav.addObject("sortName", sortFormat);
        mav.addObject("currentPage", exploreFilter.getPage());

        // For connecting all different forms
        mav.addObject("searchValue", exploreFilter.getSearch());
        mav.addObject("sortValue", exploreFilter.getSort());
        mav.addObject("statusValue", exploreFilter.getStatus());
        mav.addObject("categoryValue", exploreFilter.getCategory());
        mav.addObject("chainValue", exploreFilter.getChain());
        mav.addObject("minPriceValue", exploreFilter.getMinPrice());
        mav.addObject("maxPriceValue", exploreFilter.getMaxPrice());
        return mav;
    }

    @RequestMapping(value = "/sell/{productId}", method = RequestMethod.GET)
    public ModelAndView createSellOrderForm(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable String productId) {
        int parsedProductId = parseInt(productId);

        final ModelAndView mav = new ModelAndView("frontcontroller/sell");
        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        if(nft.getIdOwner() != user.getId())
            throw new UserIsNotNftOwnerException();
        mav.addObject("nft", nft);

        List<String> categories = categoryService.getCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    @RequestMapping(value = "/sell/{productId}", method = RequestMethod.POST)
    public ModelAndView createSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable String productId) {
        if(errors.hasErrors())
            return createSellOrderForm(form, productId);

        int parsedProductId = parseInt(productId);
        if(!nftService.currentUserOwnsNft(parsedProductId))
            throw new UserIsNotNftOwnerException();
        SellOrder sellOrder = sellOrderService.create(form.getPrice(), parsedProductId, form.getCategory()).orElseThrow(CreateSellOrderException::new);
        return new ModelAndView("redirect:/product/" + sellOrder.getNftId());
    }

    @RequestMapping(value = "/sell/update/{productId}", method = RequestMethod.GET)
    public ModelAndView getUpdateSellOrder(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        if (!nftService.userOwnsNft(parsedProductId, currentUser) && !userService.isAdmin())
            throw new UserNoPermissionException();

        final ModelAndView mav = new ModelAndView("frontcontroller/updateSellOrder");
        List<String> categories = categoryService.getCategories();
        mav.addObject("categories", categories);
        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        mav.addObject(  "nft", nft);
        SellOrder order = sellOrderService.getOrderById(nft.getSellOrder()).orElseThrow(SellOrderNotFoundException::new);
        mav.addObject("order",order);
        return mav;
    }

    @RequestMapping(value = "/sell/update/{productId}", method = RequestMethod.POST)
    public ModelAndView updateSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        if (errors.hasErrors())
            return getUpdateSellOrder(form, productId);

        if (!nftService.currentUserOwnsNft(parsedProductId) && !userService.isAdmin())
            throw new UserNoPermissionException();

        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        boolean updated = sellOrderService.update(nft.getSellOrder(), form.getCategory(), form.getPrice());
        if(updated)
            return new ModelAndView("redirect:/product/" + nft.getId());
        return getUpdateSellOrder(form, productId);
    }

    @RequestMapping(value = "/sell/delete/{productId}", method = RequestMethod.POST)
    public ModelAndView deleteSellOrder(@PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        if (!nftService.currentUserOwnsNft(parsedProductId) && !userService.isAdmin())
            throw new UserNoPermissionException();

        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);

        sellOrderService.delete(nft.getSellOrder());
        return new ModelAndView("redirect:/product/" + nft.getId());
    }

    /* Product Detail */
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ModelAndView product(@ModelAttribute("buyNftForm") final PriceForm form, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage) {
        int parsedProductId = parseInt(productId);
        int parsedOfferPage = offerPage == null ? 1 : parseInt(offerPage);
        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        User owner = userService.getUserById(nft.getIdOwner()).orElseThrow(UserNotFoundException::new);
        Optional<User> currentUser = userService.getCurrentUser();
        Optional<SellOrder> sellOrders = Optional.empty();
        List<BuyOffer> buyOffers = new ArrayList<>();
        long amountOfferPages = 0;
        if(nft.getSellOrder() != null) {
            sellOrders = sellOrderService.getOrderById(nft.getSellOrder());
            if(sellOrders.isPresent()) {
                buyOffers = buyOrderService.getOrdersBySellOrderId(parsedOfferPage, sellOrders.get().getId());
                amountOfferPages = buyOrderService.getAmountPagesBySellOrderId(sellOrders.get().getId());
            }
        }
        int favorites = favoriteService.getNftFavorites(parsedProductId);
        boolean isFaved = false;


        final ModelAndView mav = new ModelAndView("frontcontroller/product");

        if(currentUser.isPresent()) {
            isFaved = favoriteService.userFavedNft(currentUser.get().getId(), nft.getId());
            mav.addObject("isAdmin", userService.isAdmin());
        }
        mav.addObject("favorites", favorites);
        mav.addObject("nft", nft);
        mav.addObject("isFaved", isFaved);
        mav.addObject("offerPage", parsedOfferPage);
        mav.addObject("showOfferTab", offerPage != null);
        mav.addObject("amountOfferPages", amountOfferPages);
        sellOrders.ifPresent(sellOrder -> mav.addObject("sellOrder", sellOrder));
        mav.addObject("buyOffer", buyOffers);
        mav.addObject("owner", owner);
        currentUser.ifPresent(user -> mav.addObject("currentUser", currentUser.get()));

        mav.addObject("productId", parsedProductId);
        return mav;
    }

    @RequestMapping(value="/buyorder/confirm", method = RequestMethod.POST)
    public ModelAndView confirmBuyOrder(@RequestParam(value = "sellOrder") String sellOrderId, @RequestParam(value = "idBuyer") String buyerId, @RequestParam(value = "idNft") String productId, @RequestParam(value = "idSeller") String seller, @RequestParam(value = "price") BigDecimal price) {
        int parsedSellOrderId = parseInt(sellOrderId);
        int parsedBuyerId = parseInt(buyerId);
        int parsedSeller = parseInt(seller);
        int parsedProductId = parseInt(productId);
        if(nftService.currentUserOwnsNft(parsedProductId))
            throw new UserIsNotNftOwnerException();
        buyOrderService.confirmBuyOrder(parsedSellOrderId, parsedBuyerId, parsedSeller, parsedProductId, price);

        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value="/buyorder/delete", method = RequestMethod.POST)
    public ModelAndView deleteBuyOrder(@RequestParam(value = "sellOrder") String sellOrderId, @RequestParam(value = "idBuyer") String buyerId, @RequestParam(value = "idNft") String productId) {
        int parsedProductId = parseInt(productId);
        int parsedSellOrderId = parseInt(sellOrderId);
        int parsedBuyerId = parseInt(buyerId);
        if(!nftService.currentUserOwnsSellOrder(parsedProductId))
            throw new UserIsNotNftOwnerException();
        buyOrderService.deleteBuyOrder(parsedSellOrderId, parsedBuyerId);

        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("buyNftForm") final PriceForm form, final BindingResult errors, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage) {
        if (errors.hasErrors()) {
            return product(form, productId, offerPage);
        }
        int parsedProductId = parseInt(productId);

        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        SellOrder sellOrder = sellOrderService.getOrderById(nft.getSellOrder()).orElseThrow(SellOrderNotFoundException::new);
        User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        if(currentUser.getId() == nft.getIdOwner())
            throw new UserNoPermissionException();
        buyOrderService.create(sellOrder.getId(), form.getPrice(), currentUser.getId());

        ModelAndView mav = product(form, productId, offerPage);
        mav.addObject("emailSent", true);
        return mav;
    }

    @RequestMapping(value = "/product/delete/{productId}", method = RequestMethod.POST)
    public ModelAndView deleteNft(@PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        if(nftService.currentUserOwnsNft(parsedProductId))
            throw new UserIsNotNftOwnerException();
        nftService.delete(parsedProductId);

        return new ModelAndView("redirect:/explore");
    }

    @RequestMapping(value="/emailSent")
    public ModelAndView emailSent(@RequestParam(value="emailSent", required = false) String emailSent) {
        final ModelAndView mav = new ModelAndView("frontcontroller/emailSent");
        mav.addObject("emailSent", emailSent);
        return mav;
    }


    @RequestMapping( value = "/register", method = RequestMethod.GET)
    public ModelAndView createUserForm(@ModelAttribute("userForm") final UserForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/register");
        List<String> chains = chainService.getChains();
        mav.addObject("chains", chains);
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createUserForm(form);
        }
        final Optional<User> user =  userService.create(form.getEmail(), form.getUsername(), form.getWalletAddress(), form.getWalletChain(), form.getPassword());

        if (!user.isPresent()) {
            final ModelAndView mav = new ModelAndView("frontcontroller/register");
            List<String> chains = chainService.getChains();
            mav.addObject("chains", chains);
            mav.addObject("error", Boolean.TRUE);
            return mav;
        }

        return new ModelAndView("redirect:/login" );
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createNft(@ModelAttribute("createNftForm") final CreateNftForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/create");
        List<String> categories = categoryService.getCategories();
        List<String> chains = chainService.getChains();
        mav.addObject("categories", categories);
        mav.addObject("chains", chains);
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView publishNft(@Valid @ModelAttribute("createNftForm") final CreateNftForm form, final BindingResult errors) {
        if(errors.hasErrors())
            return createNft(form);
        User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        final Optional<Nft> nft = nftService.create(form.getNftId(), form.getContractAddr(), form.getName(), form.getChain(), form.getImage(), user.getId(), form.getCollection(), form.getDescription(), form.getProperties());
        if(!nft.isPresent()) {
            errors.rejectValue("publish", "publish.error", "Nft can not be created with this information");
            return createNft(form);
        }
        return new ModelAndView("redirect:/product/"+nft.get().getId());
    }

    // Login
    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("frontcontroller/login");
    }

    @RequestMapping("/current-user")
    public ModelAndView profile(@RequestParam(required = false, name = "tab") String tab){
        User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        final int userId = user.getId();
        StringBuilder redirectUrl = new StringBuilder("redirect:/profile/" + userId);
        if(tab != null){
            redirectUrl.append("?tab=").append(tab);
        }
        return new ModelAndView(redirectUrl.toString());
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView getUser(@PathVariable String userId, @RequestParam(required = false, name = "tab") String tab){
        int parsedUserId = parseInt(userId);
        ModelAndView mav = new ModelAndView("frontcontroller/profile");
        final User user = userService.getUserById(parsedUserId).orElseThrow(UserNotFoundException::new);

        User currentUser = null;
        Optional<User> optionalCurrentUser = userService.getCurrentUser();
        if(optionalCurrentUser.isPresent()) {
            currentUser = optionalCurrentUser.get();
            mav.addObject("isOwner", currentUser.getId()==user.getId());
        }
        mav.addObject("user", user);

        List<Publication> publications = new ArrayList<>();

        if (tab == null) // inventory
            publications = nftService.getAllPublicationsByUser(1, user, currentUser, false, false);
        else {
            switch (tab) {
                case "favorited":
                    if (currentUser != null && currentUser.getId() == user.getId())
                        publications = nftService.getAllPublicationsByUser(1, user, currentUser, true, false);
                    break;
                case "selling":
                    publications = nftService.getAllPublicationsByUser(1, user, currentUser, false, true);
                    break;
                case "history":
                    List<Purchase> transactions = purchaseService.getAllTransactions(parsedUserId);
                    mav.addObject("historyItems", transactions);
                    mav.addObject("historyItemsSize", transactions.size());
                    mav.addObject("showHistory", true);
                    break;
                default:
                    break;
            }
        }

        mav.addObject("publications", publications);
        mav.addObject("publicationsSize", publications.size());
        return mav;
    }

    @RequestMapping(value = "/favorite/add/{productId}", method = RequestMethod.POST)
    public String addFavorite(@PathVariable String productId, HttpServletRequest request){
        int parsedProductId = parseInt(productId);
        userService.getCurrentUser().ifPresent(user -> favoriteService.addNftFavorite(parsedProductId, user));
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @RequestMapping(value = "/favorite/remove/{productId}", method = RequestMethod.POST)
    public String removeFavorite(@PathVariable String productId, HttpServletRequest request){
        int parsedProductId = parseInt(productId);
        userService.getCurrentUser().ifPresent(user -> favoriteService.removeNftFavorite(parsedProductId, user));
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }


    @RequestMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable String id) {
        int parseId = parseInt(id);
        Image image = imageService.getImage(parseId);
        return image.getImage();
    }

    /* 404 */
    @RequestMapping("/**")
    public ModelAndView notFound() {
        return new ModelAndView("error/404");
    }

    private String capitalizeString(String s) {
        if(s != null && s.length() > 0)
            return s.substring(0,1).toUpperCase().concat(s.substring(1));
        return s;
    }
    
    private int parseInt(String number) {
        int parsedNumber;
        try {
            parsedNumber = Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid number: " + number);
        }
        return parsedNumber;
    }

}
