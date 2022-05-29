package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Review;
import ar.edu.itba.paw.model.User;

import java.util.List;

public interface ReviewDao {

    void addReview(User reviewer, User reviewee, int score, String title, String comments);

    List<Review> getUserReviews(int page, int userId, int pageSize);

    List<Review> getAllUserReviews(int userId);

    boolean hasReviewByUser(int idReviewer, int idReviewee);

    long getUserReviewsAmount(int userId);

    void deleteReview(int reviewId);
}
