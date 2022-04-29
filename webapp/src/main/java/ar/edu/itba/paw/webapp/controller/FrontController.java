package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.exceptions.SellOrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class FrontController {

    private final SellOrderService sos;
    private final CategoryService categoryService;
    private final ChainService chainService;
    private final MailingService mailingService;
    private final UserService userService;
    private final ImageService imageService;
    private final NftService nftService;

    @Autowired
    public FrontController(SellOrderService sos, CategoryService categoryService, ChainService chainService, MailingService mailingService, UserService userService, ImageService imageService, NftService nftService) {
        this.sos = sos;
        this.categoryService = categoryService;
        this.chainService = chainService;
        this.mailingService = mailingService;
        this.userService = userService;
        this.imageService = imageService;
        this.nftService = nftService;
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

        final Optional<List<Publication>> publicationsOptional = nftService.getAllPublications(1, exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(),  exploreFilter.getSearch());
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

    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    public ModelAndView createNftForm(@ModelAttribute("sellNftForm") final SellNftForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/sell");
        List<String> categories = categoryService.getCategories();
        List<String> chains = chainService.getChains();
        mav.addObject("categories", categories);
        mav.addObject("chains", chains);
        return mav;
    }

    @RequestMapping(value = "/sell", method = RequestMethod.POST)
    public ModelAndView createSellOrder(@Valid @ModelAttribute("sellNftForm") final SellNftForm form, final BindingResult errors) {
        return null;
    }

    /* Product Detail */
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ModelAndView product(@ModelAttribute("mailForm") final MailForm form, @PathVariable String productId) {
        //final NftCard nft = exploreService.getNFTById(productId, userService.getCurrentUser());
        final Optional<Nft> nft = nftService.getNFTById(productId);
        if(!nft.isPresent())
            return notFound();
        Optional<User> owner = userService.getUserById(nft.get().getId_owner());

        Optional<SellOrder> sellOrder = Optional.empty();
        if(nft.get().getSell_order() != null)
            sellOrder = sos.getOrderById(nft.get().getSell_order());

        final ModelAndView mav = new ModelAndView("frontcontroller/product");
        mav.addObject("nft", nft.get());
        sellOrder.ifPresent(order -> mav.addObject("sellorder", order));
        owner.ifPresent(user -> mav.addObject("owner", user));

        User currentUser = userService.getCurrentUser();
        if (currentUser != null)
            mav.addObject("userEmail", currentUser.getEmail());
        
        return mav;
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("mailForm") final MailForm form, final BindingResult errors, @PathVariable String productId) {
        if (errors.hasErrors()) {
            return product(form, productId);
        }

        mailingService.sendOfferMail(form.getBuyerMail(), form.getSellerMail(), form.getNftName(), form.getNftAddress(), form.getNftPrice());

        ModelAndView mav = product(form,productId);
        mav.addObject("emailSent", true);
        return mav;
    }

    @RequestMapping(value = "/product/update/{productId}", method = RequestMethod.GET)
    public ModelAndView updateSellOrder(@ModelAttribute("UpdateSellOrderForm") final UpdateSellOrderForm form, @PathVariable long productId) {
        if (!sos.isUserOwner(productId))
            return new ModelAndView("redirect:/403");

        final ModelAndView mav = new ModelAndView("frontcontroller/updateSellOrder");
        SellOrder order = sos.getOrderById(productId).orElseThrow(() -> new SellOrderNotFoundException("No existing order with id " + productId));
        List<String> categories = categoryService.getCategories();
        mav.addObject("categories", categories);
        mav.addObject("order", order);
        return mav;
    }

    @RequestMapping(value = "/product/update/{productId}", method = RequestMethod.POST)
    public ModelAndView updateSellOrder(@Valid @ModelAttribute("UpdateSellOrderForm") final UpdateSellOrderForm form, final BindingResult errors, @PathVariable long productId) {
        if (errors.hasErrors()) {
            return updateSellOrder(form, productId);
        }

        sos.update(productId, form.getCategory(), form.getPrice(), form.getDescription());
        return new ModelAndView("redirect:/product/" + productId);
    }

    @RequestMapping(value = "/product/delete/{productId}", method = RequestMethod.POST)
    public ModelAndView deleteSellOrder(@PathVariable long productId) {
        sos.delete(productId);
        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value="/emailSent")
    public ModelAndView emailSent(@RequestParam(value="emailSent", required = false) String emailSent) {
        final ModelAndView mav = new ModelAndView("frontcontroller/emailSent");
        mav.addObject("emailSent", emailSent);
        return mav;
    }

    // Create
    @RequestMapping( value = "/register", method = RequestMethod.GET )
    public ModelAndView createUserForm(@ModelAttribute("userForm") final UserForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/register");
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createUserForm(form);
        }
        final Optional<User> user =  userService.create(form.getEmail(), form.getUsername(), form.getWalletAddress(), form.getPassword());
        if (!user.isPresent()) {
            final ModelAndView mav = new ModelAndView("frontcontroller/register");
            return mav.addObject("emailExist", Boolean.TRUE);
        }

        mailingService.sendRegisterMail(user.get().getEmail(), user.get().getUsername());
        return new ModelAndView("redirect:/" );
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
        final Optional<Nft> nft = nftService.create(form.getNft_id(), form.getContract_addr(), form.getName(), form.getChain(), form.getImage(), userService.getCurrentUser().getId(), form.getCollection(), form.getDescription(), form.getProperties());
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
        final ModelAndView mav = new ModelAndView("error/404");
        return mav;
    }

    @RequestMapping("/current-user")
    public ModelAndView profile(@RequestParam(required = false, name = "tab") String tab){
        final long userId = userService.getCurrentUser().getId();
        StringBuilder redirectUrl = new StringBuilder("redirect:/profile/" + userId);
        if(tab != null){
            redirectUrl.append("?tab=").append(tab);
        }
        return new ModelAndView(redirectUrl.toString());
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView getUser(@PathVariable long userId, @RequestParam(required = false, name = "tab") String tab){
        ModelAndView mav = new ModelAndView("frontcontroller/profile");
        final Optional<User> user = userService.getUserById(userId);
        if(user.isPresent()){
            mav.addObject("user", user.get());
            List<NftCard> nfts;
            if(tab == null){
                // First tab
                nfts = sos.getUserSellOrders(user.get().getEmail());
            }
            else {
                // TODO: Make switch when more than 2 tabs
                nfts = sos.getUserFavorites(user.get().getId());
            }
            mav.addObject("nfts", nfts);
            return mav;
        }
        return new ModelAndView("redirect:/500");
    }

    @RequestMapping(value = "/favorite/add/{productId}", method = RequestMethod.POST)
    public ModelAndView addFavorite(@PathVariable long productId){
        final User user = userService.getCurrentUser();
        return sos.addFavorite(user.getId(), productId) ? new ModelAndView("redirect:/explore") : new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/favorite/remove/{productId}", method = RequestMethod.POST)
    public ModelAndView removeFavorite(@PathVariable long productId){
        final User user = userService.getCurrentUser();
        return sos.removeFavorite(user.getId(), productId) ? new ModelAndView("redirect:/explore") : new ModelAndView("redirect:/403");
    }

}
