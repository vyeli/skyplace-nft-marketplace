package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.ReviewService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.helpers.Utils.parseInt;

@Controller
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/review/{userId:\\d+}/create", method = RequestMethod.GET)
    public ModelAndView getCreateReviewFormPage(@ModelAttribute("reviewForm") final ReviewForm form, @PathVariable final int userId){
        final ModelAndView mav = new ModelAndView("frontcontroller/createReview");
        Optional<User> maybeReviewee = userService.getUserById(userId);
        Optional<User> maybeReviewer = userService.getCurrentUser();
        if(maybeReviewee.isPresent() && maybeReviewer.isPresent()) {
            mav.addObject("reviewerIdParam", maybeReviewer.get().getId());
            mav.addObject("revieweeIdParam", userId);
            mav.addObject("revieweeUsername", maybeReviewee.get().getUsername());
        }
        mav.addObject("minScore", reviewService.getMinScore());
        mav.addObject("maxScore", reviewService.getMaxScore());
        return mav;
    }

    @RequestMapping(value = "/review/{userId:\\d+}/create", method = RequestMethod.POST)
    public ModelAndView createReview(@Valid @ModelAttribute("reviewForm") final ReviewForm form, final BindingResult errors, @PathVariable final int userId){
        if(errors.hasErrors())
            return getCreateReviewFormPage(form, userId);
        int reviewerId = parseInt(form.getReviewerId());
        int revieweeId = parseInt(form.getRevieweeId());
        int score = parseInt(form.getScore());
        reviewService.addReview(reviewerId, revieweeId, score, form.getTitle(), form.getComments());
        return new ModelAndView("redirect:/profile/" + userId + "?tab=reviews");
    }

    @RequestMapping(value="/review/{userId:\\d+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteReview(@RequestParam String reviewId, @PathVariable int userId){
        int parsedReviewId = parseInt(reviewId);
        reviewService.deleteReview(parsedReviewId);
        return new ModelAndView("redirect:/profile/" + userId + "?tab=reviews");
    }
}
