package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Review;

import java.util.List;

public interface ReviewService {

    void addReview(int reviewerId, int revieweeId, int score, String title, String comments);

    List<Review> getUserReviews(int page, int userId);

    List<Review> getAllUserReviews(int userId);

    void deleteReview(int reviewId);

    int getUserReviewsPageAmount(int userId);

    long getUserReviewsAmount(int userId);

    boolean hasReviewByUser(int idReviewer, int idReviewee);

    int getPageSize();

}
