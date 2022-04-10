package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.NftCard;
import ar.edu.itba.paw.model.SellOrder;
import ar.edu.itba.paw.service.CategoryService;
import ar.edu.itba.paw.service.ChainService;
import ar.edu.itba.paw.service.ExploreService;
import ar.edu.itba.paw.service.MailingService;
import ar.edu.itba.paw.service.SellOrderService;
import ar.edu.itba.paw.webapp.form.MailForm;
import ar.edu.itba.paw.webapp.form.SellNftForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class FrontController {

    private final SellOrderService sos;
    private final CategoryService categoryService;
    private final ChainService chainService;
    private final ExploreService exploreService;
    private final MailingService mailingService;

    @Autowired
    public FrontController(SellOrderService sos, CategoryService categoryService, ChainService chainService, ExploreService exploreService, MailingService mailingService) {
        this.sos = sos;
        this.categoryService = categoryService;
        this.chainService = chainService;
        this.exploreService = exploreService;
        this.mailingService = mailingService;
    }

    @RequestMapping(value="/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("frontcontroller/index");
        return mav;
    }

    @RequestMapping("/explore")
    public ModelAndView explore() {
        final ModelAndView mav = new ModelAndView("frontcontroller/explore");
        return mav;
    }

    @RequestMapping("category/{categoryName}")
    public ModelAndView getCategory(@PathVariable String categoryName) {
        List<String> categories = new ArrayList<>(Arrays.asList("all", "collections", "art", "utility", "photography", "other"));
        if(!categories.contains(categoryName))
            return new ModelAndView("redirect:/explore");
        final ModelAndView mav = new ModelAndView("frontcontroller/category");
        final List<NftCard> nfts = exploreService.getNFTs(1);

        mav.addObject("category", categoryName);
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
        return new ModelAndView("redirect:/product/" + order.getId());
    }

    /*Product Detail*/
    @RequestMapping(value = "/product/{productId}", method = RequestMethod.GET)
    public ModelAndView product(@ModelAttribute("mailForm") final MailForm form, @PathVariable String productId) {
        long prodId = 0;
        try {
            prodId = Long.parseLong(productId);
        } catch (Exception e) {
            return new ModelAndView("frontcontroller/notfound");
        }

        final NftCard nft = exploreService.getNFTById(prodId);
        if(nft == null)
            return new ModelAndView("frontcontroller/notfound");

        final ModelAndView mav = new ModelAndView("frontcontroller/product");
        mav.addObject("nft", nft);
        return mav;
    }

    @RequestMapping(value = "/product/{productId}", method = RequestMethod.POST)
    public ModelAndView createOrder(@Valid @ModelAttribute("mailForm") final MailForm form, final BindingResult errors, @PathVariable String productId) {
        if (errors.hasErrors()) {
            return product(form, productId);
        }

        mailingService.sendMail();
        return new ModelAndView("redirect:/shipped/");
    }


    /*404*/
    @RequestMapping("/**")
    public ModelAndView notFound() {
        final ModelAndView mav = new ModelAndView("frontcontroller/notfound");
        return mav;
    }

}
