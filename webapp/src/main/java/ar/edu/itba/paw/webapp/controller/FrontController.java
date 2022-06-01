package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.auth.SkyplaceUserDetailsService;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class FrontController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);

    private final SellOrderService sellOrderService;
    private final UserService userService;
    private final ImageService imageService;
    private final NftService nftService;
    private final BuyOrderService buyOrderService;
    private final FavoriteService favoriteService;
    private final PurchaseService purchaseService;
    private final ReviewService reviewService;
    private final SkyplaceUserDetailsService userDetailsService;

    @Autowired
    public FrontController(SellOrderService sellOrderService, UserService userService, ImageService imageService, NftService nftService, BuyOrderService buyOrderService, FavoriteService favoriteService, PurchaseService purchaseService, ReviewService reviewService, SkyplaceUserDetailsService userDetailsService) {
        this.sellOrderService = sellOrderService;
        this.userService = userService;
        this.imageService = imageService;
        this.nftService = nftService;
        this.buyOrderService = buyOrderService;
        this.favoriteService = favoriteService;
        this.purchaseService = purchaseService;
        this.reviewService = reviewService;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value="/")
    public ModelAndView home() {
        return new ModelAndView("frontcontroller/index");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("frontcontroller/login");
    }

    @RequestMapping( value = "/register", method = RequestMethod.GET)
    public ModelAndView createUserForm(@ModelAttribute("userForm") final UserForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/register");
        List<String> chains = Chain.getChains();
        mav.addObject("chains", chains);
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors())
            return createUserForm(form);
            
        final User user =  userService.create(form.getEmail(), form.getUsername(), form.getWalletAddress(), form.getWalletChain(), form.getPassword());

        userDetailsService.autologin(user.getEmail(), form.getPassword());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/explore")
    public ModelAndView explore(@ModelAttribute("exploreFilter") @Valid ExploreFilter exploreFilter, HttpServletRequest request) {
        setEncodingToUTF(request);

        if(exploreFilter.getSearchFor().equals("user")) {
            final User user = userService.getUserByUsername(exploreFilter.getSearch()).orElseThrow(UserNotFoundException::new);
            return new ModelAndView(String.format("redirect:/profile/%s?searchFor=user&search=%s",user.getId(),exploreFilter.getSearch()));
        }

        List<String> categories = Category.getCategories();
        List<String> chains = Chain.getChains();

        final ModelAndView mav = new ModelAndView("frontcontroller/explore");

        final int parsedPage = parseInt(exploreFilter.getPage());
        final List<Publication> publications = nftService.getAllPublications(parsedPage, exploreFilter.getStatus(), exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(),  exploreFilter.getSearch(), exploreFilter.getSearchFor());
        int publicationsAmount = nftService.getAmountPublications(exploreFilter.getStatus(), exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(), exploreFilter.getSearch(), exploreFilter.getSearchFor());

        String lang = LocaleContextHolder.getLocale().getLanguage();
        String categoryFormat = lang.equals("es") ? "Todos" : "All";
        if(exploreFilter.getCategory() != null && !exploreFilter.getCategory().equals(""))
            if(exploreFilter.getCategory().contains(","))
                categoryFormat = lang.equals("es") ? "Varios" : "Various";
            else
                categoryFormat = capitalizeString(exploreFilter.getCategory());

        mav.addObject("category", categoryFormat);
        mav.addObject("publications", publications);
        mav.addObject("pages", (publicationsAmount-1)/nftService.getPageSize()+1);
        mav.addObject("publicationsAmount", publicationsAmount);
        mav.addObject("categories", categories);
        mav.addObject("chains", chains);
        String sortFormat = getSortStringFormat(exploreFilter.getSort());
        mav.addObject("sortName", sortFormat);
        mav.addObject("currentPage", exploreFilter.getPage());
        mav.addObject("searchValue", exploreFilter.getSearch());
        mav.addObject("sortValue", exploreFilter.getSort());
        mav.addObject("statusValue", exploreFilter.getStatus());
        mav.addObject("categoryValue", exploreFilter.getCategory());
        mav.addObject("chainValue", exploreFilter.getChain());
        mav.addObject("minPriceValue", exploreFilter.getMinPrice());
        mav.addObject("maxPriceValue", exploreFilter.getMaxPrice());
        mav.addObject("searchForValue", exploreFilter.getSearchFor());
        return mav;
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
    public ModelAndView product(@ModelAttribute("buyNftForm") final PriceForm form, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage, HttpServletRequest request) {
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
            SellOrder sellOrder = sellOrderService.getOrderById(nft.getSellOrder().getId()).orElseThrow(SellOrderNotFoundException::new);
            buyOffers = buyOrderService.getOrdersBySellOrderId(parsedOfferPage, sellOrder);
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
        return mav;
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("buyNftForm") final PriceForm form, final BindingResult errors, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage, HttpServletRequest request) {
        setEncodingToUTF(request);

        if (errors.hasErrors()) {
            return product(form, productId, offerPage, request);
        }
        int parsedProductId = parseInt(productId);

        Nft nft = nftService.getNFTById(parsedProductId).orElseThrow(NftNotFoundException::new);
        SellOrder sellOrder = sellOrderService.getOrderById(nft.getSellOrder().getId()).orElseThrow(SellOrderNotFoundException::new);
        User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        buyOrderService.create(sellOrder.getId(), form.getPrice(), currentUser.getId());

        ModelAndView mav = product(form, productId, offerPage, request);
        mav.addObject("emailSent", true);
        return mav;
    }

    @RequestMapping(value = "/product/{productId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNft(@PathVariable String productId) {
        int parsedProductId = parseInt(productId);
        nftService.delete(parsedProductId);
        return new ModelAndView("redirect:/explore");
    }

    @RequestMapping(value="/buyorder/confirm", method = RequestMethod.POST)
    public ModelAndView confirmBuyOrder(@RequestParam(value = "sellOrder") String sellOrderId, @RequestParam(value = "idBuyer") String buyerId, @RequestParam(value = "idNft") String productId, @RequestParam(value = "idSeller") String seller, @RequestParam(value = "price") BigDecimal price) {
        int parsedSellOrderId = parseInt(sellOrderId);
        int parsedBuyerId = parseInt(buyerId);
        int parsedSeller = parseInt(seller);
        int parsedProductId = parseInt(productId);
        buyOrderService.confirmBuyOrder(parsedSellOrderId, parsedBuyerId, parsedSeller, parsedProductId, price);
        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value="/buyorder/delete", method = RequestMethod.POST)
    public ModelAndView deleteBuyOrder(@RequestParam(value = "sellOrder") String sellOrderId, @RequestParam(value = "idBuyer") String buyerId, @RequestParam(value = "idNft") String productId) {
        int parsedSellOrderId = parseInt(sellOrderId);
        int parsedBuyerId = parseInt(buyerId);
        buyOrderService.deleteBuyOrder(parsedSellOrderId, parsedBuyerId);
        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createNft(@ModelAttribute("createNftForm") final CreateNftForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/create");
        List<String> categories = Category.getCategories();
        List<String> chains = Chain.getChains();
        mav.addObject("categories", categories);
        mav.addObject("chains", chains);
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView publishNft(@Valid @ModelAttribute("createNftForm") final CreateNftForm form, final BindingResult errors) {
        if(errors.hasErrors())
            return createNft(form);
        User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        final Nft nft = nftService.create(form.getNftId(), form.getContractAddr(), form.getName(), form.getChain(), form.getImage(), user.getId(), form.getCollection(), form.getDescription());
        return new ModelAndView("redirect:/product/"+nft.getId());
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
    public ModelAndView getUser(@ModelAttribute("profileFilter") @Valid ProfileFilter profileFilter, @PathVariable String userId, @RequestParam(name = "tab", required = false) String userTab){
        int parsedUserId = parseInt(userId);
        long reviewAmount;
        Double userScore = 0D;

        ModelAndView mav = new ModelAndView("frontcontroller/profile");

        final User user = userService.getUserById(parsedUserId).orElseThrow(UserNotFoundException::new);
        User currentUser = null;
        Optional<User> optionalCurrentUser = userService.getCurrentUser();

        if(optionalCurrentUser.isPresent()) {
            currentUser = optionalCurrentUser.get();
            mav.addObject("isOwner", currentUser.getId() == user.getId());
            mav.addObject("currentUserId", currentUser.getId());
        }
        mav.addObject("user", user);

        List<Review> allReviews = reviewService.getAllUserReviews(user.getId());
        reviewAmount = allReviews.size();
        for(Review r : allReviews)
            userScore += r.getScore();
        userScore /= reviewAmount;

        String tabName = (userTab != null ? userTab : profileFilter.getTab());
        tabName = tabName != null ? tabName : "inventory";

        switch(tabName) {
            case "reviews":
                List<Review> reviews = reviewService.getUserReviews(Integer.parseInt(profileFilter.getPage()), user.getId());
                mav.addObject("canReview", currentUser != null && reviewService.purchaseExists(currentUser.getId(), parsedUserId));
                mav.addObject("reviews", reviews);
                mav.addObject("pages", getPageAmount(reviewAmount, reviewService.getPageSize()));
                optionalCurrentUser.ifPresent(value -> mav.addObject("hasReviewByUser", reviewService.hasReviewByUser(value.getId(), user.getId())));
                // The 'right' approach, but as review amount is retrieved anyway its done differently
                // mav.addObject("pages", reviewService.getUserReviewsPageAmount(user.getId()));

                Map<Integer, Double> starRatings = new HashMap<>();
                for(Review r : allReviews){
                    starRatings.put(r.getScore(), starRatings.getOrDefault(r.getScore(), 0D) + 1);
                }
                for(int i=1 ; i <= 5 ; i++){
                    double percentage = starRatings.getOrDefault(i, 0D) * 100 / reviewAmount;
                    mav.addObject("percentageStars" + i, (int) percentage);
                }
                break;
            case "history":
                List<Purchase> transactions = purchaseService.getAllTransactions(parsedUserId, Integer.parseInt(profileFilter.getPage()));
                mav.addObject("historyItems", transactions);
                mav.addObject("historyItemsSize", transactions.size());
                mav.addObject("pages", purchaseService.getAmountPagesByUserId(parsedUserId));
                break;
            case "buyOrders":
                List<BuyOrder> buyOrders = buyOrderService.getBuyOrdersForUser(user, Integer.parseInt(profileFilter.getPage()));
                mav.addObject("buyOrderItems", buyOrders);
                mav.addObject("buyOrderItemsSize", buyOrders.size());
                mav.addObject("pages", buyOrderService.getAmountPagesForUser(user));
                break;
            default:
                List<Publication> publications = nftService.getAllPublicationsByUser(Integer.parseInt(profileFilter.getPage()), user, tabName, profileFilter.getSort());
                int publicationPages = nftService.getAmountPublicationPagesByUser(user, currentUser, tabName);
                mav.addObject("publications", publications);
                mav.addObject("publicationsSize", publications.size());
                mav.addObject("pages", publicationPages);
                break;
        }

        String sortFormat = getSortStringFormat(profileFilter.getSort());

        mav.addObject("userScore", round(userScore, 2));
        mav.addObject("reviewAmount", reviewAmount);
        mav.addObject("tabName", tabName);
        mav.addObject("sortName", sortFormat);
        mav.addObject("sortValue", profileFilter.getSort());
        mav.addObject("currentPage", profileFilter.getPage());
        mav.addObject("isAdmin", userService.isAdmin());
        return mav;
    }

    @RequestMapping(value = "/favorite/{productId}/add", method = RequestMethod.POST)
    public String addFavorite(@PathVariable String productId, HttpServletRequest request){
        int parsedProductId = parseInt(productId);
        userService.getCurrentUser().ifPresent(user -> favoriteService.addNftFavorite(parsedProductId, user));
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @RequestMapping(value = "/favorite/{productId}/remove", method = RequestMethod.POST)
    public String removeFavorite(@PathVariable String productId, HttpServletRequest request){
        int parsedProductId = parseInt(productId);
        userService.getCurrentUser().ifPresent(user -> favoriteService.removeNftFavorite(parsedProductId, user));
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @RequestMapping(value = "/review/{userId}/create", method = RequestMethod.GET)
    public ModelAndView getCreateReviewFormPage(@ModelAttribute("reviewForm") final ReviewForm form, @PathVariable final String userId){
        final int parsedUserId = parseInt(userId);
        final ModelAndView mav = new ModelAndView("frontcontroller/createReview");
        Optional<User> maybeReviewee = userService.getUserById(parsedUserId);
        Optional<User> maybeReviewer = userService.getCurrentUser();
        if(maybeReviewee.isPresent() && maybeReviewer.isPresent()) {
            mav.addObject("reviewerIdParam", maybeReviewer.get().getId());
            mav.addObject("revieweeIdParam", parsedUserId);
            mav.addObject("revieweeUsername", maybeReviewee.get().getUsername());
        }
        return mav;
    }

    @RequestMapping(value = "/review/{userId}/create", method = RequestMethod.POST)
    public ModelAndView createReview(@Valid @ModelAttribute("reviewForm") final ReviewForm form, final BindingResult errors, @PathVariable final String userId){
        if(errors.hasErrors())
            return getCreateReviewFormPage(form, userId);
        int reviewerId = parseInt(form.getReviewerId());
        int revieweeId = parseInt(form.getRevieweeId());
        int score = parseInt(form.getScore());
        reviewService.addReview(reviewerId, revieweeId, score, form.getTitle(), form.getComments());
        return new ModelAndView("redirect:/profile/" + userId + "?tab=reviews");
    }

    @RequestMapping(value="/review/{userId}/delete", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam String reviewId, @PathVariable String userId){
        int parsedReviewId = parseInt(reviewId);
        reviewService.deleteReview(parsedReviewId);
        return new ModelAndView("redirect:/profile/" + userId + "?tab=reviews");
    }

    @RequestMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable String id) {
        int parseId = parseInt(id);
        Image image = imageService.getImage(parseId).orElseThrow(ImageNotFoundException::new);
        return image.getImage();
    }

    private double round(double number, int decimals){
        double rounder = Math.pow(10, decimals);
        return Math.round(number * rounder) / rounder;
    }

    private long getPageAmount(long itemAmount, int pageSize){
        return itemAmount == 0 ? 1 : (itemAmount-1)/pageSize + 1;
    }

    private String capitalizeString(String s) {
        if(s != null && s.length() > 0)
            return s.substring(0,1).toUpperCase().concat(s.substring(1));
        return s;
    }

    private void setEncodingToUTF(HttpServletRequest request) {
        try {
            request.setCharacterEncoding("utf-8");
        }
        catch(UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    private int parseInt(String number) throws NumberFormatException {
        int parsedNumber;
        parsedNumber = Integer.parseInt(number);
        return parsedNumber;
    }

    private String getSortStringFormat(String sort) {
        Locale locale = LocaleContextHolder.getLocale();
        if (!Objects.equals(locale.getLanguage(), "es")) {
            switch(sort) {
                case "priceAsc":
                    return "Price: Low to High";
                case "priceDsc":
                    return "Price: High to Low";
                case "collection":
                    return "Collection";
                default:
                    return "Name";
            }
        }
        switch(sort) {
            case "priceAsc":
                return "Precio: Menor a Mayor";
            case "priceDsc":
                return "Precio: Mayor a menor";
            case "collection":
                return "Colección";
            default:
                return "Nombre";
        }
    }

}
