package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ar.edu.itba.paw.webapp.helpers.Utils.*;

@Controller
public class FrontController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);

    private final UserService userService;
    private final NftService nftService;
    private final MessageSource messageSource;

    @Autowired
    public FrontController(UserService userService, NftService nftService, MessageSource messageSource) {
        this.userService = userService;
        this.nftService = nftService;
        this.messageSource = messageSource;
    }

    @RequestMapping(value="/")
    public ModelAndView home() {
        return new ModelAndView("frontcontroller/index");
    }

    @RequestMapping("/explore")
    public ModelAndView explore(@ModelAttribute("exploreFilter") @Valid ExploreFilter exploreFilter, HttpServletRequest request) {
        setEncodingToUTF(request);

        List<String> categories = Category.getCategories();
        List<String> chains = Chain.getChains();

        final ModelAndView mav = new ModelAndView("frontcontroller/explore");

        final int parsedPage = parseInt(exploreFilter.getPage());
        final List<Publication> publications = nftService.getAllPublications(parsedPage, exploreFilter.getStatus(), exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(),  exploreFilter.getSearch(), exploreFilter.getSearchFor());
        int publicationsAmount = nftService.getAmountPublications(exploreFilter.getStatus(), exploreFilter.getCategory(), exploreFilter.getChain(), exploreFilter.getMinPrice(), exploreFilter.getMaxPrice(), exploreFilter.getSort(), exploreFilter.getSearch(), exploreFilter.getSearchFor());

        String categoryFormat = messageSource.getMessage("explore.categoryFormat.All", null, LocaleContextHolder.getLocale());
        if(exploreFilter.getCategory() != null && !exploreFilter.getCategory().equals(""))
            if(exploreFilter.getCategory().contains(","))
                categoryFormat = messageSource.getMessage("explore.categoryFormat.Various", null, LocaleContextHolder.getLocale());
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

}
