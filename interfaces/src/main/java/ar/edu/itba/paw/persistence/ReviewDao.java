package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Review;

public interface ReviewDao {

    void addReview(int idReviewer, int idReviewee, int score, String comments);

}
