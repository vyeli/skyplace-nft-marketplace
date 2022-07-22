//package ar.edu.itba.paw.webapp.controller;
//
//import ar.edu.itba.paw.exceptions.UserNotFoundException;
//import ar.edu.itba.paw.exceptions.UserNotLoggedInException;
//import ar.edu.itba.paw.model.*;
//import ar.edu.itba.paw.service.*;
//import ar.edu.itba.paw.webapp.form.ProfileFilter;
//import ar.edu.itba.paw.webapp.helpers.BuyOrderItemType;
//import ar.edu.itba.paw.webapp.helpers.Tab;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.validation.Valid;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static ar.edu.itba.paw.webapp.helpers.Utils.*;
//
//@Controller
//public class ProfileController {
//
//    private final UserService userService;
//    private final ReviewService reviewService;
//    private final BuyOrderService buyOrderService;
//    private final NftService nftService;
//    private final PurchaseService purchaseService;
//
//    @Autowired
//    public ProfileController(UserService userService, ReviewService reviewService, BuyOrderService buyOrderService, NftService nftService, PurchaseService purchaseService) {
//        this.userService = userService;
//        this.reviewService = reviewService;
//        this.buyOrderService = buyOrderService;
//        this.nftService = nftService;
//        this.purchaseService = purchaseService;
//    }
//
//    @RequestMapping("/current-user")
//    public ModelAndView profile(@RequestParam(required = false, name = "tab") String tab){
//        User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
//        final int userId = user.getId();
//        StringBuilder redirectUrl = new StringBuilder("redirect:/profile/" + userId);
//        if(tab != null){
//            redirectUrl.append("?tab=").append(tab);
//        }
//        return new ModelAndView(redirectUrl.toString());
//    }
//
//    @RequestMapping("/profile/{userId:\\d+}")
//    public ModelAndView getUser(@ModelAttribute("profileFilter") @Valid ProfileFilter profileFilter, @PathVariable int userId, @RequestParam(name = "tab", required = false) String userTab){
//        int parsedPage = parseInt(profileFilter.getPage());
//
//        ModelAndView mav = new ModelAndView("frontcontroller/profile");
//
//        final User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
//        Optional<User> currentUser = userService.getCurrentUser();
//
//        boolean isOwner = false;
//        if(currentUser.isPresent()) {
//            isOwner = currentUser.get().getId() == userId;
//            mav.addObject("isOwner", isOwner);
//            mav.addObject("currentUserId", currentUser.get().getId());
//        }
//        mav.addObject("user", user);
//
//        String tabName = userTab != null ? userTab : "inventory";
//        Map<String, Tab> userTabs = getUserTabs();
//
//        final boolean hasAllTabs = isOwner;
//        if(userTabs.getOrDefault(tabName, null) == null || (!isOwner && !userTabs.get(tabName).isPublic())) {
//            tabName = "inventory";
//        }
//        List<Tab> tabList = userTabs.values().stream().filter(t -> t.isPublic() || hasAllTabs).sorted().collect(Collectors.toList());
//        userTabs.get(tabName).setActive(true);
//
//        switch(tabName) {
//            case "reviews":
//                mav.addObject("reviews", reviewService.getUserReviews(parsedPage, userId));
//                mav.addObject("canReview", currentUser.isPresent() && reviewService.purchaseExists(currentUser.get().getId(), userId) && !reviewService.hasReviewByUser(currentUser.get().getId(), userId));
//                mav.addObject("pages", reviewService.getUserReviewsPageAmount(userId));
//                mav.addObject("ratings", reviewService.getUserReviewsRatingsListSorted(userId, "desc"));
//                mav.addObject("minScore", reviewService.getMinScore());
//                mav.addObject("maxScore", reviewService.getMaxScore());
//                break;
//            case "history":
//                List<Purchase> transactions = purchaseService.getAllTransactions(userId, Integer.parseInt(profileFilter.getPage()));
//                mav.addObject("historyItems", transactions);
//                mav.addObject("historyItemsSize", transactions.size());
//                mav.addObject("pages", purchaseService.getAmountPagesByUserId(userId));
//                break;
//            case "buyorders":
//                String itemsTypeName = buyOrderService.hasValidFilterName(profileFilter.getItems()) ? profileFilter.getItems() : "ALL";
//                List<BuyOrder> buyOrders = buyOrderService.getBuyOrdersForUser(user, Integer.parseInt(profileFilter.getPage()), itemsTypeName);
//                mav.addObject("buyOrderItems", buyOrders);
//                mav.addObject("buyOrderItemsSize", buyOrders.size());
//                mav.addObject("pages", buyOrderService.getAmountPagesForUser(user));
//                mav.addObject("isMySale", itemsTypeName.equals("MYSALES"));
//                Map<String, BuyOrderItemType> itemTypes = getBuyOrderItemTypes();
//                itemTypes.get(itemsTypeName).setActive(true);
//                mav.addObject("buyOrderItemTypes", itemTypes.values());
//                break;
//            default:
//                List<Publication> publications = nftService.getAllPublicationsByUser(Integer.parseInt(profileFilter.getPage()), user, tabName, profileFilter.getSort());
//                int publicationPages = nftService.getAmountPublicationPagesByUser(user, currentUser.orElse(null), tabName);
//                mav.addObject("publications", publications);
//                mav.addObject("publicationsSize", publications.size());
//                mav.addObject("pages", publicationPages);
//                break;
//        }
//
//        String sortFormat = getSortStringFormat(profileFilter.getSort());
//
//        mav.addObject("userScore", round(reviewService.getUserScore(userId), 2));
//        mav.addObject("reviewAmount", reviewService.getUserReviewsAmount(userId));
//        mav.addObject("tabs", tabList);
//        mav.addObject("sortName", sortFormat);
//        mav.addObject("sortValue", profileFilter.getSort());
//        mav.addObject("currentPage", profileFilter.getPage());
//        mav.addObject("isAdmin", userService.isAdmin());
//        return mav;
//    }
//
//    private Map<String, Tab> getUserTabs(){
//        Map<String, Tab> userTabs = new HashMap<>();
//        userTabs.put("inventory", new Tab(0, "inventory", true, false));
//        userTabs.put("selling", new Tab(1, "selling", true, false));
//        userTabs.put("buyorders", new Tab(2, "buyorders", false, false));
//        userTabs.put("favorited", new Tab(3,  "favorited", false, false));
//        userTabs.put("history", new Tab(4, "history", false, false));
//        userTabs.put("reviews", new Tab(5, "reviews", true, false));
//        return userTabs;
//    }
//
//    private Map<String, BuyOrderItemType> getBuyOrderItemTypes(){
//        Map<String, BuyOrderItemType> buyOrderItemTypes = new HashMap<>();
//        buyOrderItemTypes.put("ALL", new BuyOrderItemType("ALL", false));
//        for(String s : buyOrderService.getBuyOrderStatusNames())
//            buyOrderItemTypes.put(s, new BuyOrderItemType(s, false));
//        return buyOrderItemTypes;
//    }
//}
