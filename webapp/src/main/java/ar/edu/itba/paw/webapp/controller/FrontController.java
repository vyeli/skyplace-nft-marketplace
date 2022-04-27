package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.webapp.form.ExploreFilter;
import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.exceptions.SellOrderNotFoundException;
import ar.edu.itba.paw.webapp.form.MailForm;
import ar.edu.itba.paw.webapp.form.SellNftForm;
import ar.edu.itba.paw.webapp.form.UpdateSellOrderForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@Controller
public class FrontController {

    private final SellOrderService sos;
    private final CategoryService categoryService;
    private final ChainService chainService;
    private final ExploreService exploreService;
    private final MailingService mailingService;
    private final UserService userService;
    private final ImageService imageService;

    @Autowired
    public FrontController(SellOrderService sos, CategoryService categoryService, ChainService chainService, ExploreService exploreService, MailingService mailingService, UserService userService, ImageService imageService) {
        this.sos = sos;
        this.categoryService = categoryService;
        this.chainService = chainService;
        this.exploreService = exploreService;
        this.mailingService = mailingService;
        this.userService = userService;
        this.imageService = imageService;
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
        final List<NftCard> nfts = exploreService.getNFTs(1, exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(),  exploreFilter.getSearch());

        if(exploreFilter.getCategory().contains(","))
            exploreFilter.setCategory("Various");
        else
            exploreFilter.setCategory(exploreFilter.getCategory().substring(0,1).toUpperCase()+exploreFilter.getCategory().substring(1));

        mav.addObject("category", exploreFilter.getCategory());
        mav.addObject("nfts", nfts);
        mav.addObject("pages", nfts.size()/12+1);
        mav.addObject("nftAmount", nfts.size());
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
        if (errors.hasErrors()) {
            return createNftForm(form);
        }

        final SellOrder order = sos.create(form.getName(), form.getNftId(), form.getNftContract(), form.getChain(), form.getCategory(), form.getPrice(), form.getDescription(), form.getImage());
        if(order.getId() == -1) {
            errors.rejectValue("publish", "publish.error", "Order can not be created with this information");
            return createNftForm(form);
        }
        return new ModelAndView("redirect:/product/" + order.getId());
    }

    /* Product Detail */
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ModelAndView product(@ModelAttribute("mailForm") final MailForm form, @PathVariable String productId) {
        final NftCard nft = exploreService.getNFTById(productId);
        if(nft == null)
            return notFound();

        final ModelAndView mav = new ModelAndView("frontcontroller/product");
        mav.addObject("nft", nft);

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

    @RequestMapping("/profile")
    public ModelAndView profile(@RequestParam(required = false, name = "tab") String tab){
        ModelAndView mav = new ModelAndView("frontcontroller/profile");

        final User user = userService.getCurrentUser();
        List<NftCard> nfts;
        if(tab != null && tab.equals("favorited")){
            nfts = sos.getUserFavorites(user.getId());
        } else {
            nfts = sos.getUserSellOrders(user.getEmail());
        }
        mav.addObject("user", user);
        mav.addObject("nfts", nfts);
        return mav;
    }

    @RequestMapping(value = "/favorite/add/{productId}", method = RequestMethod.POST)
    public ModelAndView addFavorite(@PathVariable long productId){
        final User user = userService.getCurrentUser();
        return sos.addFavorite(user.getId(), productId) ? new ModelAndView("redirect:/") : new ModelAndView("redirect:/403");
    }

    @RequestMapping(value = "/favorite/remove/{productId}", method = RequestMethod.POST)
    public ModelAndView removeFavorite(@PathVariable long productId){
        final User user = userService.getCurrentUser();
        return sos.removeFavorite(user.getId(), productId) ? new ModelAndView("redirect:/") : new ModelAndView("redirect:/403");
    }

}
