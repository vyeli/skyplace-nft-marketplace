package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.InvalidReviewException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.ReviewDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewDao reviewDao;
    private final UserDao userDao;

    private final MailingService mailingService;
    private final static int pageSize = 5;
    private final static int minScore = 1;
    private final static int maxScore = 5;

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, UserDao userDao, MailingService mailingService) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
        this.mailingService = mailingService;
    }

    @Transactional
    @Override
    public void addReview(int reviewerId, int revieweeId, int score, String title, String comments) {
        if(reviewerId == revieweeId || hasReviewByUser(reviewerId, revieweeId))
            throw new InvalidReviewException();
        User reviewer = userDao.getUserById(reviewerId).orElseThrow(UserNotFoundException::new);
        User reviewee = userDao.getUserById(revieweeId).orElseThrow(UserNotFoundException::new);
        Review newReview = reviewDao.addReview(reviewer, reviewee, score, title, comments);
        mailingService.sendNewReviewMail(newReview, LocaleContextHolder.getLocale());
    }

    @Override
    public List<Review> getUserReviews(int page, int userId) {
        if(!userDao.getUserById(userId).isPresent())
            throw new UserNotFoundException();
        return reviewDao.getUserReviews(page, userId, pageSize);
    }

    @Override
    public List<Review> getAllUserReviews(int userId) {
        if(!userDao.getUserById(userId).isPresent())
            throw new UserNotFoundException();
        return reviewDao.getAllUserReviews(userId);
    }

    @Override
    public double getUserScore(int userId) {
        if(!userDao.getUserById(userId).isPresent())
            throw new UserNotFoundException();
        return reviewDao.getUserScore(userId);
    }

    @Override
    public Map<Integer, Integer> getUserReviewsRatings(int userId) {
        if(!userDao.getUserById(userId).isPresent())
            throw new UserNotFoundException();
        return reviewDao.getUserReviewsRatings(userId, minScore, maxScore);
    }

    @Override
    public List<Integer> getUserReviewsRatingsListSorted(int userId, String sort) {
        if(!userDao.getUserById(userId).isPresent())
            throw new UserNotFoundException();
        return reviewDao.getUserReviewsRatingsListSorted(userId, minScore, maxScore, sort);
    }

    @Override
    public int getUserReviewsPageAmount(int userId) {
        if(!userDao.getUserById(userId).isPresent())
            throw new UserNotFoundException();
        long userReviewsAmount = reviewDao.getUserReviewsAmount(userId);
        if(userReviewsAmount == 0)
            return 1;
        return (int)(userReviewsAmount-1)/pageSize + 1;
    }

    @Override
    public boolean hasReviewByUser(int idReviewer, int idReviewee) {
        if(!userDao.getUserById(idReviewer).isPresent() ||
                !userDao.getUserById(idReviewee).isPresent())
            throw new UserNotFoundException();
        return reviewDao.hasReviewByUser(idReviewer, idReviewee);
    }

    @Override
    public long getUserReviewsAmount(int userId) {
        if(!userDao.getUserById(userId).isPresent())
            throw new UserNotFoundException();
        return reviewDao.getUserReviewsAmount(userId);
    }

    @Transactional
    @Override
    public void deleteReview(int reviewId) {
        reviewDao.deleteReview(reviewId);
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public int getMinScore() {
        return minScore;
    }

    @Override
    public int getMaxScore() {
        return maxScore;
    }

    @Override
    public boolean purchaseExists(int buyerId, int sellerId) {
        return reviewDao.purchaseExists(buyerId, sellerId);
    }
}
