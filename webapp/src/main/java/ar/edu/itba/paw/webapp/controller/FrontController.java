package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.exceptions.SellOrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class FrontController {

    private final SellOrderService sellOrderService;
    private final CategoryService categoryService;
    private final ChainService chainService;
    private final MailingService mailingService;
    private final UserService userService;
    private final ImageService imageService;
    private final NftService nftService;
    private final BuyOrderService buyOrderService;
    private final FavoriteService favoriteService;

    @Autowired
    public FrontController(SellOrderService sellOrderService, CategoryService categoryService, ChainService chainService, MailingService mailingService, UserService userService, ImageService imageService, NftService nftService, BuyOrderService buyOrderService, FavoriteService favoriteService) {
        this.sellOrderService = sellOrderService;
        this.categoryService = categoryService;
        this.chainService = chainService;
        this.mailingService = mailingService;
        this.userService = userService;
        this.imageService = imageService;
        this.nftService = nftService;
        this.buyOrderService = buyOrderService;
        this.favoriteService = favoriteService;
    }

    @RequestMapping(value="/")
    public ModelAndView home(@RequestParam(value="emailSent", required = false) String emailSent) {
        final ModelAndView mav = new ModelAndView("frontcontroller/index");
        mav.addObject("emailSent", emailSent);
        return mav;
    }

    @RequestMapping("/explore")
    public ModelAndView explore(@ModelAttribute("exploreFilter") @Valid ExploreFilter exploreFilter) {

        List<String> categories = categoryService.getCategories();
        List<String> chains = chainService.getChains();

        final ModelAndView mav = new ModelAndView("frontcontroller/explore");

        Optional<User> currentUserOptional = userService.getCurrentUser();
        User currentUser = null;
        if(currentUserOptional.isPresent())
            currentUser = currentUserOptional.get();

        final Optional<List<Publication>> publicationsOptional = nftService.getAllPublications(1, exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(),  exploreFilter.getSearch(), currentUser);
        if(!publicationsOptional.isPresent())
            return notFound();
        final List<Publication> publications = publicationsOptional.get();

        if(exploreFilter.getCategory().contains(","))
            exploreFilter.setCategory("Various");
        else
            exploreFilter.setCategory(exploreFilter.getCategory().substring(0,1).toUpperCase()+exploreFilter.getCategory().substring(1));

        mav.addObject("category", exploreFilter.getCategory());
        mav.addObject("publications", publications);
        mav.addObject("pages", publications.size()/12+1);
        mav.addObject("publicationsAmount", publications.size());
        mav.addObject("categories", categories);
        mav.addObject("chains", chains);

        return mav;
    }

    @RequestMapping(value = "/sell/{productId}", method = RequestMethod.GET)
    public ModelAndView createSellOrderForm(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable String productId) {
        final ModelAndView mav = new ModelAndView("frontcontroller/sell");
        Optional<Nft> nft = nftService.getNFTById(productId);
        if(!nft.isPresent())
            return notFound();
        Optional<User> user = userService.getCurrentUser();
        if(!user.isPresent())
            return notFound();
        if(user.get().getId() != nft.get().getIdOwner())
            return new ModelAndView("redirect:/403");
        mav.addObject("nft", nft.get());
        List<String> categories = categoryService.getCategories();
        mav.addObject("categories", categories);
        return mav;
    }

    @RequestMapping(value = "/sell/{productId}", method = RequestMethod.POST)
    public ModelAndView createSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable String productId) {
        if(errors.hasErrors())
            return createSellOrderForm(form, productId);

        Optional<SellOrder> sellOrder = sellOrderService.create(form.getPrice(), productId, form.getCategory());
        return sellOrder.map(order -> new ModelAndView("redirect:/product/" + order.getNftId())).orElseGet(() -> createSellOrderForm(form, productId));
    }

    @RequestMapping(value = "/sell/update/{productId}", method = RequestMethod.GET)
    public ModelAndView getUpdateSellOrder(@ModelAttribute("sellNftForm") final SellNftForm form, @PathVariable String productId) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(!currentUser.isPresent())
            return new ModelAndView("redirect:/403");

        if (!nftService.userOwnsNft(productId, currentUser.get()) && !userService.isAdmin())
            return new ModelAndView("redirect:/403");

        final ModelAndView mav = new ModelAndView("frontcontroller/updateSellOrder");
        List<String> categories = categoryService.getCategories();
        mav.addObject("categories", categories);
        Optional<Nft> nft = nftService.getNFTById(productId);
        if(!nft.isPresent() || nft.get().getSellOrder() == null)
            return notFound();
        mav.addObject(  "nft", nft.get());
        Optional<SellOrder> order = sellOrderService.getOrderById(nft.get().getSellOrder());
        if(!order.isPresent())
            return notFound();
        mav.addObject("order",order.get());
        return mav;
    }

    @RequestMapping(value = "/sell/update/{productId}", method = RequestMethod.POST)
    public ModelAndView updateSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors, @PathVariable String productId) {
        if (errors.hasErrors()) {
            return getUpdateSellOrder(form, productId);
        }

        Optional<User> currentUser = userService.getCurrentUser();
        if(!currentUser.isPresent())
            return new ModelAndView("redirect:/403");

        if (!nftService.userOwnsNft(productId, currentUser.get()) && !userService.isAdmin())
            return new ModelAndView("redirect:/403");

        Optional<Nft> nft = nftService.getNFTById(productId);
        if(!nft.isPresent() || nft.get().getSellOrder() == null)
            return notFound();
        sellOrderService.update(nft.get().getSellOrder(), form.getCategory(), form.getPrice());
        return new ModelAndView("redirect:/product/" + nft.get().getId());
    }

    @RequestMapping(value = "/sell/delete/{productId}", method = RequestMethod.POST)
    public ModelAndView deleteSellOrder(@PathVariable String productId) {
        Optional<User> currentUser = userService.getCurrentUser();
        if(!currentUser.isPresent())
            return new ModelAndView("redirect:/403");

        if (!nftService.userOwnsNft(productId, currentUser.get()) && !userService.isAdmin())
            return new ModelAndView("redirect:/403");

        Optional<Nft> nft = nftService.getNFTById(productId);
        if(!nft.isPresent() || nft.get().getSellOrder() == null)
            return notFound();

        sellOrderService.delete(nft.get().getSellOrder());
        return new ModelAndView("redirect:/product/" + nft.get().getId());
    }

    /* Product Detail */
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ModelAndView product(@ModelAttribute("buyNftForm") final BuyNftForm form, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage) {
        final Optional<Nft> nft = nftService.getNFTById(productId);
        if(!nft.isPresent())
            return notFound();
        Optional<User> owner = userService.getUserById(nft.get().getIdOwner());
        Optional<User> currentUser = userService.getCurrentUser();
        Optional<SellOrder> sellOrders = Optional.empty();
        Optional<List<BuyOffer>> buyOffers = Optional.empty();
        long amountOfferPages = 0;
        if(nft.get().getSellOrder() != null) {
            sellOrders = sellOrderService.getOrderById(nft.get().getSellOrder());
            if(sellOrders.isPresent()) {
                buyOffers = buyOrderService.getOrdersBySellOrderId(offerPage, sellOrders.get().getId());
                amountOfferPages = buyOrderService.getAmountPagesBySellOrderId(sellOrders.get().getId());
            }
        }
        long favorites = favoriteService.getNftFavorites(productId);
        boolean isFaved = false;
        if(currentUser.isPresent())
            isFaved = favoriteService.userFavedNft(currentUser.get().getId(), nft.get().getId());

        final ModelAndView mav = new ModelAndView("frontcontroller/product");
        mav.addObject("favorites", favorites);
        mav.addObject("nft", nft.get());
        mav.addObject("isFaved", isFaved);
        long offPage = 1;
        try {
            offPage = Long.parseLong(offerPage);
        } catch(Exception ignored){}
        if(offPage < 1)
            offPage = 1;
        mav.addObject("offerPage", offPage);
        mav.addObject("showOfferTab", offerPage != null);
        mav.addObject("amountOfferPages", amountOfferPages);
        sellOrders.ifPresent(sellOrder -> mav.addObject("sellOrder", sellOrder));
        buyOffers.ifPresent(buyOffer -> mav.addObject("buyOffer", buyOffer));
        owner.ifPresent(user -> mav.addObject("owner", user));
        currentUser.ifPresent(user -> mav.addObject("currentUser", currentUser.get()));

        mav.addObject("productId", productId);
        return mav;
    }

    @RequestMapping(value="/buyorder/confirm", method = RequestMethod.POST)
    public ModelAndView confirmBuyOrder(@RequestParam(value = "sellOrder") String sellorder, @RequestParam(value = "buyer") String buyer, @RequestParam(value = "product") String productId) {
        buyOrderService.confirmBuyOrder(sellorder, buyer);

        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value="/buyorder/delete", method = RequestMethod.POST)
    public ModelAndView deleteBuyOrder(@RequestParam(value = "sellOrder") String sellorder, @RequestParam(value = "buyer") String buyer, @RequestParam(value = "product") String productId) {
        buyOrderService.deleteBuyOrder(sellorder, buyer);

        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("buyNftForm") final BuyNftForm form, final BindingResult errors, @PathVariable String productId, @RequestParam(value = "offerPage", required = false) String offerPage) {
        if (errors.hasErrors()) {
            return product(form, productId, offerPage);
        }

        Optional<Nft> nft = nftService.getNFTById(productId);
        if(!nft.isPresent())
            return product(form, productId, offerPage);
        if(nft.get().getSellOrder() == null)
            return product(form, productId, offerPage);
        Optional<SellOrder> sellOrder = sellOrderService.getOrderById(nft.get().getSellOrder());
        if(!sellOrder.isPresent())
            return product(form, productId, offerPage);
        Optional<User> currentUser = userService.getCurrentUser();

        if(!currentUser.isPresent())
            return product(form, productId, offerPage);
        if(currentUser.get().getId() == nft.get().getIdOwner())
            return product(form, productId, offerPage);
        Optional<User> seller = userService.getUserById(nft.get().getIdOwner());
        if(!seller.isPresent())
            return product(form, productId, offerPage);
        buyOrderService.create(sellOrder.get().getId(), form.getPrice(), currentUser.get().getId());

        // mailingService.sendOfferMail(currentUser.get().getEmail(), seller.get().getEmail(), nft.get().getNftName(), nft.get().getContractAddr(), form.getPrice());

        ModelAndView mav = product(form,productId, offerPage);
        mav.addObject("emailSent", true);
        return mav;
    }

    @RequestMapping(value = "/product/delete/{productId}", method = RequestMethod.POST)
    public ModelAndView deleteNft(@PathVariable String productId) {
        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value="/emailSent")
    public ModelAndView emailSent(@RequestParam(value="emailSent", required = false) String emailSent) {
        final ModelAndView mav = new ModelAndView("frontcontroller/emailSent");
        mav.addObject("emailSent", emailSent);
        return mav;
    }

    // Create
    @RequestMapping( value = "/register", method = RequestMethod.GET)
    public ModelAndView createUserForm(@ModelAttribute("userForm") final UserForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/register");
        List<String> chains = chainService.getChains();
        mav.addObject("chains", chains);
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors, HttpServletRequest request) {
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

        mailingService.sendRegisterMail(user.get().getEmail(), user.get().getUsername());
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
        Optional<User> user = userService.getCurrentUser();
        if(!user.isPresent())
            return createNft(form);
        final Optional<Nft> nft = nftService.create(form.getNftId(), form.getContractAddr(), form.getName(), form.getChain(), form.getImage(), user.get().getId(), form.getCollection(), form.getDescription(), form.getProperties());
        if(!nft.isPresent()) {
            errors.rejectValue("publish", "publish.error", "Nft can not be created with this information");
            return createNft(form);
        }
        return new ModelAndView("redirect:/product/"+nft.get().getId());
    }

    // Login
    @RequestMapping("/login")
    public ModelAndView login() {
        final ModelAndView mav = new ModelAndView("frontcontroller/login");
        return mav;
    }

    @RequestMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable long id) {
        Image image = imageService.getImage(id);
        return image.getImage();
    }

    /* 404 */
    @RequestMapping("/**")
    public ModelAndView notFound() {
        return new ModelAndView("error/404");
    }

    @RequestMapping("/current-user")
    public ModelAndView profile(@RequestParam(required = false, name = "tab") String tab){
        Optional<User> user = userService.getCurrentUser();
        if(!user.isPresent())
            return notFound();
        final long userId = user.get().getId();
        StringBuilder redirectUrl = new StringBuilder("redirect:/profile/" + userId);
        if(tab != null){
            redirectUrl.append("?tab=").append(tab);
        }
        return new ModelAndView(redirectUrl.toString());
    }

    // TODO: Remove mapping once mails are fully integrated
    @RequestMapping("/mail")
    public ModelAndView mailTest(){
        // mailingService.sendRegisterMail("spiegarejr@gmail.com", "spiegarejr");
        Optional<Nft> nft = nftService.getNFTById("1");
        if(!nft.isPresent())
            return new ModelAndView("redirect:/404");
        mailingService.sendOfferMail("mlbanchini1970@gmail.com", "spiegarejr@gmail.com", nft.get().getNftName(), nft.get().getNftId(), nft.get().getContractAddr(), BigDecimal.valueOf((float)0.015), imageService.getImage(nft.get().getIdImage()));
        return new ModelAndView("redirect:/");
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView getUser(@PathVariable String userId, @RequestParam(required = false, name = "tab") String tab){
        ModelAndView mav = new ModelAndView("frontcontroller/profile");
        final Optional<User> user = userService.getUserById(userId);
        User currentUser = null;
        Optional<User> optionalCurrentUser = userService.getCurrentUser();
        if(optionalCurrentUser.isPresent())
            currentUser = optionalCurrentUser.get();
        if(user.isPresent()){
            mav.addObject("user", user.get());
            Optional<List<Publication>> publications;
            if(tab == null)
                publications = nftService.getAllPublicationsByUser(1, user.get(), currentUser, false, false);
            else if(tab.equals("favorited"))
                publications = nftService.getAllPublicationsByUser(1, user.get(), currentUser, true, false);
            else
                publications = nftService.getAllPublicationsByUser(1, user.get(), currentUser, false, true);
            publications.ifPresent(publication -> {
                mav.addObject("publications", publication);
                mav.addObject("publicationsSize", publication.size());
            });
            return mav;
        }
        return new ModelAndView("redirect:/404");
    }

    @RequestMapping(value = "/favorite/add/{productId}", method = RequestMethod.POST)
    public String addFavorite(@PathVariable String productId, HttpServletRequest request){
        userService.getCurrentUser().ifPresent(user -> favoriteService.addNftFavorite(productId, user));
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @RequestMapping(value = "/favorite/remove/{productId}", method = RequestMethod.POST)
    public String removeFavorite(@PathVariable String productId, HttpServletRequest request){
        userService.getCurrentUser().ifPresent(user -> favoriteService.removeNftFavorite(productId, user));
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @RequestMapping("/newProduct")
    public ModelAndView newProduct() {
        final ModelAndView mav = new ModelAndView("product");
        return mav;
    }

}
