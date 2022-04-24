package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.MailForm;
import ar.edu.itba.paw.webapp.form.SellNftForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    @Autowired
    public FrontController(SellOrderService sos, CategoryService categoryService, ChainService chainService, ExploreService exploreService, MailingService mailingService, UserService userService) {
        this.sos = sos;
        this.categoryService = categoryService;
        this.chainService = chainService;
        this.exploreService = exploreService;
        this.mailingService = mailingService;
        this.userService = userService;
    }

    @RequestMapping(value="/")
    public ModelAndView home(@RequestParam(value="emailSent", required = false) String emailSent) {
        final ModelAndView mav = new ModelAndView("frontcontroller/index");
        mav.addObject("emailSent", emailSent);
        return mav;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public ModelAndView search() {

        return new ModelAndView("redirect:/category");
    }

    @RequestMapping("/explore")
    public ModelAndView explore() {
        final ModelAndView mav = new ModelAndView("frontcontroller/explore");
        return mav;
    }

    @RequestMapping("category/{categoryName}")
    public ModelAndView getCategory(@PathVariable String categoryName, @RequestParam(value="name", required=false) String name) {
        List<String> categories = categoryService.getStaticCategories();
        if(!categories.contains(categoryName))
            return new ModelAndView("redirect:/explore");
        final ModelAndView mav = new ModelAndView("frontcontroller/category");
        final List<NftCard> nfts = exploreService.getNFTs(1, categoryName, name);

        mav.addObject("category", categoryName.substring(0,1).toUpperCase()+categoryName.substring(1));
        mav.addObject("nfts", nfts);
        mav.addObject("pages", nfts.size()/12+1);
        mav.addObject("nftAmount", nfts.size());
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

        final SellOrder order = sos.create(form.getName(), form.getNftId(), form.getNftContract(), form.getChain(), form.getCategory(), form.getPrice(), form.getDescription(), form.getImage(), form.getEmail());
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
        return mav;
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("mailForm") final MailForm form, final BindingResult errors, @PathVariable String productId, ModelMap model) {
        if (errors.hasErrors()) {
            return product(form, productId);
        }

        final boolean returnSatus = mailingService.sendMail(form.getBuyerMail(), form.getSellerMail(), form.getNftName(), form.getNftAddress(), form.getNftPrice());
        model.addAttribute("emailSent", returnSatus);
        return new ModelAndView("redirect:/emailSent", model);
    }


    @RequestMapping(value="/emailSent")
    public ModelAndView emailSent(@RequestParam(value="emailSent", required = false) String emailSent) {
        final ModelAndView mav = new ModelAndView("frontcontroller/emailSent");
        mav.addObject("emailSent", emailSent);
        return mav;
    }

    // Create
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView createUserForm(@ModelAttribute("userForm") final UserForm form) {
        final ModelAndView mav = new ModelAndView("frontcontroller/register");
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createUser(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createUserForm(form);
        }

        final User user =  userService.create(form.getEmail(), form.getUsername(), form.getWalletAddress(), form.getPassword());
        return new ModelAndView("redirect:/" );
    }

    // Login
    @RequestMapping("/login")
    public ModelAndView login() {
        final ModelAndView mav = new ModelAndView("frontcontroller/login");
        return mav;
    }



    /* 404 */
    @RequestMapping("/**")
    public ModelAndView notFound() {
        final ModelAndView mav = new ModelAndView("error/404");
        return mav;
    }

    @RequestMapping("/profile")
    public ModelAndView profile(){
        ModelAndView mav = new ModelAndView("frontcontroller/profile");

        final User user = userService.getCurrentUser();
        final List<NftCard> nfts = exploreService.getNFTs(1, "All", null);
        mav.addObject("user", user);
        mav.addObject("nfts", nfts);
        return mav;
    }

}
